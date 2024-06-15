package com.parg3v.data.gesture

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription

interface GestureHandler {
    fun performGesture(gesture: GestureDescription, callback: AccessibilityService.GestureResultCallback)
}