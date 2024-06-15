package com.parg3v.data.repository

import android.util.Log
import com.parg3v.domain.common.config.ServerConfig
import com.parg3v.domain.repository.GestureLogRepository
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
import javax.inject.Inject
import kotlin.random.Random

class ServerRepositoryImpl @Inject constructor(
    private val gestureLogRepository: GestureLogRepository
) : ServerRepository {

    private var server: ApplicationEngine? = null

    override suspend fun startServer(port: Int) {
        server = embeddedServer(Netty, host = ServerConfig.IP, port = port) {
            install(WebSockets)
            routing {
                webSocket("/ws") { // WebSocket endpoint
                    send("You are connected to the server.")
                    Log.d("ServerRepo", "startServer: connected")
                    launch  {
                        gestureLogRepository.logGesture("Client connected")
                    }

                    try {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    val receivedText = frame.readText()
                                    Log.d("WebSocketServer", "Received: $receivedText")
                                    send("Server received: $receivedText")
                                    if (receivedText.contains("Gesture completed")) {
                                        launch  {
                                            gestureLogRepository.logGesture(receivedText)
                                        }
                                    }
                                }
                                is Frame.Binary -> {
                                    val receivedBytes = frame.readBytes()
                                    Log.d("WebSocketServer", "startServer: $receivedBytes")
                                }
                                else -> {}
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("WebSocketServer", "Error: ${e.localizedMessage}")
                    } finally {
                        Log.d("WebSocketServer", "Client disconnected")
                        launch  {
                            gestureLogRepository.logGesture("Client disconnected")
                        }
                    }
                }
            }
        }.start(wait = false)
        Log.d("WebSocketServer", "Server started on ${ServerConfig.IP}:$port")
    }

    override suspend fun stopServer() {
        server?.stop(1000, 2000)
        Log.d("WebSocketServer", "Server stopped")
    }

    override fun generateRandomGesture(): String {
        val startY = Random.nextInt(500, 1500)
        val endY = Random.nextInt(500, 1500)
        return if (startY > endY) {
            "Swipe up from $startY to $endY"
        } else {
            "Swipe down from $startY to $endY"
        }
    }

    override fun sendGestures() {
        server?.application?.launch {
            while (true) {
                delay(2000)
                val gesture = generateRandomGesture()
                sendGestureToClients(gesture)
            }
        }
    }

    private suspend fun sendGestureToClients(gesture: String) {
        server?.application?.launch {
            val sessions = mutableListOf<DefaultWebSocketSession>()
            sessions.forEach { session ->
                session.send(Frame.Text(gesture))
                launch  {
                    gestureLogRepository.logGesture("Sent gesture: $gesture")
                }
            }
        }
    }
}