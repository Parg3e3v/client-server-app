package com.parg3v.client

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.view.accessibility.AccessibilityEvent
import com.parg3v.data.gesture.GestureHandler


class MyAccessibilityService : AccessibilityService(), GestureHandler {
    companion object {
        private var instance: MyAccessibilityService? = null

        fun getInstance(): MyAccessibilityService? = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // TODO Handle accessibility events if needed
    }

    override fun onInterrupt() {
        // TODO Handle interrupt
    }

    override fun performGesture(gesture: GestureDescription, callback: GestureResultCallback) {
        dispatchGesture(gesture, callback, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}