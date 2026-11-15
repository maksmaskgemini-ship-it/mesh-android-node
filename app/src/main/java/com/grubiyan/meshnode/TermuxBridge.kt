package com.grubiyan.meshnode

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object TermuxBridge {
    private const val PKG = "com.termux"
    private const val PERM = "com.termux.permission.RUN_COMMAND"

    fun isInstalled(ctx: Context): Boolean = try {
        ctx.packageManager.getPackageInfo(PKG, 0); true
    } catch (e: PackageManager.NameNotFoundException) { false }

    fun runCommand(ctx: Context, cmd: String) {
        if (ContextCompat.checkSelfPermission(ctx, PERM) != PackageManager.PERMISSION_GRANTED) return
        val i = Intent().apply {
            setClassName(PKG, "com.termux.app.RunCommandService")
            action = "com.termux.RUN_COMMAND"
            putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
            putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", cmd))
            putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home")
            putExtra("com.termux.RUN_COMMAND_BACKGROUND", true)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            ctx.startForegroundService(i) else ctx.startService(i)
    }
}
