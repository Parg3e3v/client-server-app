package com.parg3v.data.repository

import android.util.Log
import com.parg3v.domain.common.config.ServerConfig
import com.parg3v.domain.repository.ServerRepository
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import io.ktor.websocket.send
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor() : ServerRepository {

    private var server: ApplicationEngine? = null

    override fun startServer(port: Int) {
        server = embeddedServer(Netty, host = ServerConfig.IP, port = port) {
            install(WebSockets)
            routing {
                webSocket("/ws") { // This is the WebSocket endpoint
                    send("You are connected to the server.")
                    Log.d("ServerRepo", "startServer: connected")

                    try {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    val receivedText = frame.readText()
                                    Log.d("WebSocketServer", "Received: $receivedText")
                                    send("Server received: $receivedText")
                                }
                                is Frame.Binary -> {
                                    val receivedBytes = frame.readBytes()
                                    Log.d("ServerRepo", "startServer: $receivedBytes")
                                }
                                else -> {}
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("WebSocketServer", "Error: ${e.localizedMessage}")
                    } finally {
                        Log.d("WebSocketServer", "Client disconnected")
                    }
                }
            }
        }.start(wait = false)
        Log.d("WebSocketServer", "Server started on ${ServerConfig.IP}:$port")
    }

    override fun stopServer() {
        server?.stop(1000, 2000)
        Log.d("WebSocketServer", "Server stopped")
    }
}