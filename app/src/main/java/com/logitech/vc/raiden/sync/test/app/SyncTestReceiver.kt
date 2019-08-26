package com.logitech.vc.raiden.sync.test.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService

class SyncTestReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val it = Intent(context.applicationContext, SyncTestService::class.java)
       context.applicationContext.startForegroundService(it)
    }
}
