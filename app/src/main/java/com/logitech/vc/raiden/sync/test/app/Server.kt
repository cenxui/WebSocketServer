package com.logitech.vc.raiden.sync.test.app

import android.app.Activity
import android.content.Context
import android.widget.Toast
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

import java.net.InetSocketAddress
import java.nio.ByteBuffer

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */

class Server(port: Int, private val context: Context)  : WebSocketServer(InetSocketAddress(port)) {

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        conn.send("Welcome to the server!") //This method sends a message to the new client
        broadcast("new connection: " + handshake.resourceDescriptor) //This method sends a message to all clients connected
        println(conn.remoteSocketAddress.address.hostAddress + " entered the room!")
        toast(conn.remoteSocketAddress.address.hostAddress + " entered the room!")
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        broadcast("$conn has left the room!")
        println("$conn has left the room!")
        toast("$conn has left the room!")
    }

    override fun onMessage(conn: WebSocket, message: String) {
        broadcast(message)
        println("$conn: $message")
        toast(message)
    }

    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        broadcast(message!!.array())
        println(conn.toString() + ": " + message)
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