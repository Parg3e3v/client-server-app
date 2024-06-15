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
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor(
    private val gestureLogRepositoryImpl: GestureLogRepositoryImpl
) : ServerRepository {

    private var server: ApplicationEngine? = null

    override fun startServer(port: Int) {
        server = embeddedServer(Netty, host = ServerConfig.IP, port = port) {
            install(WebSockets)
            routing {
                webSocket("/ws") { // WebSocket endpoint
                    send("You are connected to the server.")
                    Log.d("ServerRepo", "startServer: connected")
                    runBlocking {
                        gestureLogRepositoryImpl.logGesture("Client connected")
                    }

                    try {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    val receivedText = frame.readText()
                                    Log.d("WebSocketServer", "Received: $receivedText")
                                    send("Server received: $receivedText")
                                    if (receivedText.contains("Gesture completed")) {
                                        runBlocking {
                                            gestureLogRepositoryImpl.logGesture(receivedText)
                                        }
                                    }
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
                        runBlocking {
                            gestureLogRepositoryImpl.logGesture("Client disconnected")
                        }
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

    override fun sendGestures() {
        server?.application?.launch {
            val gestures = listOf("Swipe up", "Swipe down", "Swipe up large", "Swipe down large")
            while (true) {
                for (gesture in gestures) {
                    delay(2000)
                    sendGestureToClients(gesture)
                }
            }
        }
    }

    private suspend fun sendGestureToClients(gesture: String) {
        server?.application?.launch {
            val sessions = mutableListOf<DefaultWebSocketSession>()
            sessions.forEach { session ->
                session.send(Frame.Text(gesture))
                runBlocking {
                    gestureLogRepositoryImpl.logGesture("Sent gesture: $gesture")
                }
            }
        }
    }
}