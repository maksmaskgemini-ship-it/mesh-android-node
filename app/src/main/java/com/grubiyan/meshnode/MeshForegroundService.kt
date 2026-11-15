package com.grubiyan.meshnode

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MeshForegroundService : Service() {
    override fun onBind(i: Intent?): IBinder? = null
    override fun onStartCommand(i: Intent?, f: Int, s: Int): Int {
        val ch = "mesh_node"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(ch) == null)
                nm.createNotificationChannel(NotificationChannel(ch, "Mesh Node", NotificationManager.IMPORTANCE_LOW))
        }
        val n = NotificationCompat.Builder(this, ch)
            .setContentTitle("Mesh Node").setContentText("Узел работает")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setOngoing(true).build()
        startForeground(1, n)
        return START_STICKY
    }
}
