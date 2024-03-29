package com.logitech.vc.raiden.sync.test.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.logitech.vc.raiden.sync.test.isync.ISync
import com.logitech.vc.raiden.sync.test.isync.ISyncCallback

class SyncTestService : Service() {

    private val TAG = SyncTestService::class.java.name

    private lateinit var server :Server

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

        val port = 8888 // 843 flash policy port
        server = Server(port, this)
        server.start()
        Log.d(TAG, "Server started on port: " + server.port)
        Log.d(TAG, "Service onStartCommand called.")
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {

        return binder
    }

    private val binder = object : ISync.Stub() {
        override fun registerClient(clientId: String?, callback: ISyncCallback) {
            server.callback = callback
        }

        override fun unregisterClient(clientId: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun sendMessage(message: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
