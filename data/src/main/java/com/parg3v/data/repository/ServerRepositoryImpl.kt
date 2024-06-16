package com.parg3v.data.repository

import android.util.Log
import com.parg3v.domain.repository.GestureLogRepository
import com.parg3v.domain.repository.ServerRepository
import com.parg3v.domain.utils.getLocalIpAddress
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import kotlin.random.Random

class ServerRepositoryImpl @Inject constructor(
    private val gestureLogRepository: GestureLogRepository
) : ServerRepository {

    private var server: ApplicationEngine? = null
    private val serverScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val sessions = CopyOnWriteArrayList<DefaultWebSocketSession>()

    override suspend fun startServer(port: Int) {
        val localIp = getLocalIpAddress() ?: throw IllegalStateException("Unable to get local IP address")
        server = embeddedServer(Netty, host = localIp, port = port) {
            install(WebSockets)
            routing {
                webSocket("/ws") {
                    sessions.add(this)
                    handleWebSocketConnection(this)
                }
            }
        }.start(wait = false)
        Log.d("WebSocketChat [Server]", "Server started on $localIp:$port")
    }

    private suspend fun handleWebSocketConnection(session: DefaultWebSocketSession) {
        session.send("You are connected to the server.")
        Log.d("WebSocketChat [Server]", "startServer: connected")
        serverScope.launch {
            gestureLogRepository.logGesture("Client connected")
        }

        try {
            for (frame in session.incoming) {
                when (frame) {
                    is Frame.Text -> handleTextFrame(frame)
                    is Frame.Binary -> handleBinaryFrame(frame)
                    else -> {}
                }
            }
        } catch (e: Exception) {
            Log.e("WebSocketChat [Server]", "Error: ${e.localizedMessage}")
        } finally {
            Log.d("WebSocketChat [Server]", "Client disconnected")
            serverScope.launch {
                gestureLogRepository.logGesture("Client disconnected")
            }
            sessions.remove(session)
        }
    }

    private suspend fun handleTextFrame(frame: Frame.Text) {
        val receivedText = frame.readText()
        Log.d("WebSocketChat [Server]", "Received: $receivedText")
        delay(3000)
        when {
            receivedText == "Browser is open" -> handleBrowserOpen()
            receivedText.contains("Gesture") -> {
                Log.d("WebSocketChat [Server]", "handleTextFrame: Server received callback, sending a gesture...")
                sendGestures()
                serverScope.launch {
                    gestureLogRepository.logGesture(receivedText)
                }
            }
        }
    }

    private fun handleBinaryFrame(frame: Frame.Binary) {
        val receivedBytes = frame.readBytes()
        Log.d("WebSocketChat [Server]", "startServer: $receivedBytes")
    }

    override suspend fun stopServer() {
        sessions.forEach { session ->
            try {
                session.close(CloseReason(CloseReason.Codes.NORMAL, "Server is stopping"))
            } catch (e: Exception) {
                Log.e("WebSocketChat [Server]", "Error closing session: ${e.localizedMessage}")
            }
        }

        serverScope.coroutineContext[Job]?.cancelAndJoin()

        server?.stop(1000, 2000)
        Log.d("WebSocketChat [Server]", "Server stopped")
    }

    override suspend fun handleBrowserOpen() {
        Log.d("WebSocketChat [Server]", "Browser is open")
        sendGestures()
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
        Log.d("WebSocketChat [Server]", "sendGestures: on")
        serverScope.launch {
            delay(2000)
            val gesture = generateRandomGesture()
            sendGestureToClients(gesture)
        }
    }

    private suspend fun sendGestureToClients(gesture: String) {
        sessions.forEach { session ->
            try {
                Log.e("WebSocketChat [Server]", "gesture sent: ${Frame.Text(gesture)}")
                session.send(Frame.Text(gesture))
                serverScope.launch {
                    gestureLogRepository.logGesture("Sent gesture: $gesture")
                }
            } catch (e: Exception) {
                Log.e("WebSocketChat [Server]", "Failed to send gesture: ${e.localizedMessage}")
            }
        }
    }
}