package com.parg3v.data.repository

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Path
import android.net.Uri
import android.util.Log
import com.parg3v.data.gesture.GestureHandler
import com.parg3v.data.local.GestureLogDao
import com.parg3v.data.local.GestureLogEntity
import com.parg3v.domain.repository.ClientRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val context: Context,
    private val gestureLogDao: GestureLogDao,
    private val gestureHandler: GestureHandler?
) : ClientRepository {

    private var client: HttpClient? = null
    private var webSocketSession: WebSocketSession? = null

    override suspend fun startClient(ip: String, port: Int) {
        client = HttpClient(OkHttp) {
            install(WebSockets)
        }
        withContext(Dispatchers.IO) {
            client?.ws(
                method = HttpMethod.Get,
                host = ip,
                port = port,
                path = "/ws"
            ) {
                webSocketSession = this

                Log.d("WebSocketChat [Client]", "Connected to server")


                withContext(Dispatchers.Main) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }

                webSocketSession?.send("Browser is open")

                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val receivedText = frame.readText()
                                Log.d("WebSocketChat [Client]", "Received: $receivedText")
                                gestureLogDao.insert(GestureLogEntity(message = "Received: $receivedText"))
                                performGesture(receivedText)
                            }

                            is Frame.Binary -> {
                                val receivedBytes = frame.readBytes()
                                Log.d("WebSocketChat [Client]", "Received: $receivedBytes")
                            }

                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocketChat [Client]", "Error: ${e.localizedMessage}")
                } finally {
                    Log.d("WebSocketChat [Client]", "Disconnected from server")
                    withContext(Dispatchers.IO) {
                        gestureLogDao.insert(GestureLogEntity(message = "Disconnected from server"))
                    }
                }
            }
        }
    }

    override fun stopClient() {
        CoroutineScope(Dispatchers.IO).launch {
            webSocketSession?.close()
            client?.close()
            Log.d("WebSocketClient  [Client]", "Client stopped")
        }
    }

    override suspend fun performGesture(gesture: String) {
        val regex = Regex("Swipe (up|down) from (\\d+) to (\\d+)")
        val matchResult = regex.matchEntire(gesture)
        if (matchResult != null) {
            val gestureDescription = GestureDescription.Builder()

            val startY = matchResult.groupValues[2].toFloat()
            val endY = matchResult.groupValues[3].toFloat()

            val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
            if (startY in 0f..screenHeight && endY in 0f..screenHeight) {

                val path = Path().apply {
                    moveTo(500f, startY)
                    lineTo(500f, endY)
                }

                gestureDescription.addStroke(
                    GestureDescription.StrokeDescription(path, 0, 1000)
                ).build()

                gestureHandler?.performGesture(
                    gestureDescription.build(),
                    object : AccessibilityService.GestureResultCallback() {
                        override fun onCompleted(gestureDescription: GestureDescription?) {
                            CoroutineScope(Dispatchers.IO).launch {
                                Log.d("WebSocketClient [Client]", "Gesture completed: $gesture")
                                webSocketSession?.send("Gesture completed: $gesture")
                                gestureLogDao.insert(GestureLogEntity(message = "Gesture completed: $gesture"))
                            }
                            super.onCompleted(gestureDescription)
                        }

                        override fun onCancelled(gestureDescription: GestureDescription?) {
                            CoroutineScope(Dispatchers.IO).launch {
                                Log.e("WebSocketClient [Client]", "Gesture cancelled: $gesture")
                                webSocketSession?.send("Gesture cancelled: $gesture")
                                gestureLogDao.insert(GestureLogEntity(message = "Gesture cancelled: $gesture"))
                            }
                            super.onCancelled(gestureDescription)
                        }
                    }) ?: run {
                    Log.e("WebSocketClient [Client]", "GestureHandler instance is null")
                }
            } else {
                webSocketSession?.send("Gesture coordinates out of bounds: $gesture")
                Log.e("WebSocketClient [Client]", "Gesture coordinates out of bounds: $gesture")
            }
        } else {
            webSocketSession?.send("Gesture info not correct: $gesture")
            Log.e("WebSocketClient [Client]", "Gesture info not correct: $gesture")
        }
    }
}