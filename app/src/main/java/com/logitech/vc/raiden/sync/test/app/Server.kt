package com.logitech.vc.raiden.sync.test.app

import android.content.Context
import android.content.Intent
import android.widget.Toast
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import android.content.ComponentName
import com.logitech.vc.raiden.sync.test.isync.ISyncCallback


/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */

class Server(port: Int, private val context: Context)  : WebSocketServer(InetSocketAddress(port)) {

    private val demoapp = "com.logitech.vc.raiden.sync.test.demoapp"
    private val demoappAcitivity = "com.logitech.vc.raiden.sync.test.demoapp.MainActivity"

    lateinit var callback: ISyncCallback

    //todo
    private val map = HashMap<String, WebSocket>()

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        conn.send("Welcome to the server!") //This method sends a message to the new client
        broadcast("new connection: " + handshake.resourceDescriptor) //This method sends a message to all clients connected
        toast(conn.remoteSocketAddress.address.hostAddress + " entered the room!")
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        broadcast("$conn has left the room!")
        toast("$conn has left the room!")
    }

    override fun onMessage(conn: WebSocket, message: String) {
        conn.send("server : $message")
        toast(message)

        if (message.trim().equals("start")){
            startDemoApp()
        }else if (callback != null) {
            conn.send(callback.onMessage(message))
        }
    }

    private fun startDemoApp() {
        val it = Intent(Intent.ACTION_VIEW)
        context.packageManager.getLaunchIntentForPackage(demoapp)
        it.setComponent(ComponentName(demoapp, demoappAcitivity))
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(it)
    }

    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        broadcast(message!!.array())
        toast(conn.toString() + ": " + message)
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        ex.printStackTrace()
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    override fun onStart() {
        println("Server started!")
        connectionLostTimeout = 0
        connectionLostTimeout = 100
    }

    private fun toast(message: String) {
        context.applicationContext.mainExecutor.execute(
            (Runnable {
                Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT)
                    .show()
            }
        ))
    }
}