package com.parg3v.client

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.parg3v.data.gesture.GestureHandler
import com.parg3v.data.local.GestureLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyAccessibilityService : AccessibilityService(), GestureHandler {
    companion object {
        private var instance: MyAccessibilityService? = null

        fun getInstance(): MyAccessibilityService? = instance
    }

    fun testGesture() {
        val path = Path().apply {
            moveTo(500f, 0f)
            lineTo(500f, 900f)
        }
        val gestureDescription = GestureDescription.Builder().addStroke(
            GestureDescription.StrokeDescription(path, 0, 1000) // Increased duration to 1000ms
        ).build()
        performGesture(gestureDescription, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d("MyAccessibilityService", "Gesture completed in test")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.e("MyAccessibilityService", "Gesture cancelled in test")
            }
        })
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d("MyAccessibilityService", "Service Created")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onInterrupt() {
        // TODO Handle interrupt
    }

    override fun performGesture(gesture: GestureDescription, callback: GestureResultCallback) {
        Log.d("Ac Service", "performGesture: Trying to swipe...")
        dispatchGesture(gesture, callback, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

}