package com.logitech.vc.raiden.sync.test.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class SyncTestService : Service() {

    private val TAG = SyncTestService::class.java.name

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = NotificationChannel(
            this.javaClass.name,
            BuildConfig.NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_DEFAULT)

        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)

        val notification = Notification.Builder(
            applicationContext,
            channel.id)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .build()

        startForeground(BuildConfig.NOTIFICATION_ID, notification)

        val port = 8887 // 843 flash policy port
        val s = Server(port, this)
        s.start()
        Log.d(TAG, "Server started on port: " + s.port)
        Log.d(TAG, "Service onStartCommand called.")
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
