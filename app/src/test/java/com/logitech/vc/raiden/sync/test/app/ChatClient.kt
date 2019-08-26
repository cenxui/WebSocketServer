package com.logitech.vc.raiden.sync.test.app

import java.net.URI
import java.net.URISyntaxException

import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.handshake.ServerHandshake
import java.io.BufferedReader
import java.io.InputStreamReader


/** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded.  */
class ChatClient : WebSocketClient {

    constructor(serverUri: URI, draft: Draft) : super(serverUri, draft) {}

    constructor(serverURI: URI) : super(serverURI) {}

    constructor(serverUri: URI, httpHeaders: Map<String, String>) : super(serverUri, httpHeaders) {}

    override fun onOpen(handshakedata: ServerHandshake) {
        send("Hello, it is me. Mario :)")
        println("opened connection")
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    override fun onMessage(message: String) {
        println("received: $message")
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        println("Connection closed by " + (if (remote) "remote peer" else "us") + " Code: " + code + " Reason: " + reason)
    }

    override fun onError(ex: Exception) {
        ex.printStackTrace()
        // if the error is fatal then onClose will be called additionally
    }

    companion object {

        @Throws(URISyntaxException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val c =
                ChatClient(URI("ws://10.200.248.0:8888")) // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
            c.connect()
            val sysin = BufferedReader(InputStreamReader(System.`in`))
            while (true) {
                val `in` = sysin.readLine()
                c.send(`in`)
                if (`in` == "exit") {
                    c.close(1000)
                    break
                }
            }
        }
    }

}