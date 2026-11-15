package com.grubiyan.meshnode

import android.content.Context
import java.io.File
import java.util.zip.ZipInputStream

object TermuxBootstrap {
    private const val BOOTSTRAP_ASSET = "bootstrap-aarch64.zip"
    private const val MARKER = ".bootstrap_done"

    fun ensureBootstrapped(ctx: Context, onDone: (Boolean) -> Unit) {
        Thread {
            try {
                val prefix = File(ctx.filesDir, "usr")
                val marker = File(prefix, MARKER)
                if (marker.exists()) { onDone(true); return@Thread }

                prefix.mkdirs()
                ctx.assets.open(BOOTSTRAP_ASSET).use { input ->
                    ZipInputStream(input).use { zis ->
                        var e = zis.nextEntry
                        while (e != null) {
                            val out = File(prefix, e.name)
                            if (e.isDirectory) out.mkdirs()
                            else {
                                out.parentFile?.mkdirs()
                                out.outputStream().use { zis.copyTo(it) }
                                if (!e.name.endsWith(".so")) out.setExecutable(true)
                            }
                            zis.closeEntry()
                            e = zis.nextEntry
                        }
                    }
                }
                marker.writeText("ok")
                onDone(true)
            } catch (e: Exception) {
                onDone(false)
            }
        }.start()
    }
}
