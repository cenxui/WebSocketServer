package com.logitech.vc.raiden.sync.test.demoapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.logitech.vc.raiden.sync.test.isync.ISync
import com.logitech.vc.raiden.sync.test.isync.ISyncCallback

class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * Intent action. todo
         */
        private const val SERVICE_ACTION = "com.logitech.vc.raiden.sync.test.app.SyncTestService"

        /**
         * Package of the service to bind to.
         */
        private const val SERVICE_PACKAGE = "com.logitech.vc.raiden.sync.test.app"
    }


    private val connection = object : ServiceConnection {

        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service
            val iSync = ISync.Stub.asInterface(service)
            iSync.registerClient("camera", object : ISyncCallback.Stub() {
                override fun onMessage(message: String) :String {
                    return "camera : $message"
                }
            })
        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Service has unexpectedly disconnected")
            iSync = null
        }
    }

    private val TAG = MainActivity::class.java.name

    private var iSync: ISync? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(SERVICE_ACTION).setPackage(SERVICE_PACKAGE)

        if (!bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)) {
            throw RuntimeException("Unable to bind to service!")
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
}
