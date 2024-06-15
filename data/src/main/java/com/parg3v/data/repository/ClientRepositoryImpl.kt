package com.parg3v.data.repository

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.Intent
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
import kotlinx.coroutines.runBlocking
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
        client?.ws(
            method = HttpMethod.Get,
            host = ip,
            port = port,
            path = "/ws"
        ) {
            webSocketSession = this
            Log.d("WebSocketClient", "Connected to server")
            runBlocking {
                gestureLogDao.insert(GestureLogEntity(message = "Connected to server"))
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("http://www.google.com")
            context.startActivity(intent)

            try {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val receivedText = frame.readText()
                            Log.d("WebSocketClient", "Received: $receivedText")
                            runBlocking {
                                gestureLogDao.insert(GestureLogEntity(message = "Received: $receivedText"))
                            }
                            performGesture(receivedText)
                        }

                        is Frame.Binary -> {
                            val receivedBytes = frame.readBytes()
                            Log.d("WebSocketClient", "Received: $receivedBytes")
                        }

                        else -> {}
                    }
                }
            } catch (e: Exception) {
                Log.e("WebSocketClient", "Error: ${e.localizedMessage}")
            } finally {
                Log.d("WebSocketClient", "Disconnected from server")
                runBlocking {
                    gestureLogDao.insert(GestureLogEntity(message = "Disconnected from server"))
                }
            }
        }
    }

    override fun stopClient() {
        CoroutineScope(Dispatchers.IO).launch {
            webSocketSession?.close()
            client?.close()
            Log.d("WebSocketClient", "Client stopped")
        }
    }

    override fun performGesture(gesture: String) {
        val regex = Regex("Swipe (up|down) from (\\d+) to (\\d+)")
        val matchResult = regex.matchEntire(gesture)
        if (matchResult != null) {
            val startY = matchResult.groupValues[2].toFloat()
            val endY = matchResult.groupValues[3].toFloat()

            val path = Path().apply {
                moveTo(500f, startY)
                lineTo(500f, endY)
            }

            val gestureDescription = GestureDescription.Builder().addStroke(
                GestureDescription.StrokeDescription(path, 0, 500)
            ).build()

            gestureHandler?.performGesture(
                gestureDescription,
                object : AccessibilityService.GestureResultCallback() {
                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        super.onCompleted(gestureDescription)
                        CoroutineScope(Dispatchers.IO).launch {
                            webSocketSession?.send("Gesture completed: $gesture")
                            gestureLogDao.insert(GestureLogEntity(message = "Gesture completed: $gesture"))
                        }
                    }

                    override fun onCancelled(gestureDescription: GestureDescription?) {
                        super.onCancelled(gestureDescription)
                        CoroutineScope(Dispatchers.IO).launch {
                            webSocketSession?.send("Gesture cancelled: $gesture")
                            gestureLogDao.insert(GestureLogEntity(message = "Gesture cancelled: $gesture"))
                        }
                    }
                }) ?: run {
                Log.e("WebSocketClient", "GestureHandler instance is null")
            }
        }
    }

}