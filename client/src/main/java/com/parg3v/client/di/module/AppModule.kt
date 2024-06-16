package com.parg3v.client.di.module

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.util.Log
import com.parg3v.client.MyAccessibilityService
import com.parg3v.data.gesture.GestureHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGestureHandler(): GestureHandler {
        return MyAccessibilityService.getInstance() ?: object : GestureHandler {
            override fun performGesture(gesture: GestureDescription, callback: AccessibilityService.GestureResultCallback) {
                Log.e("GestureHandler", "Fallback GestureHandler: MyAccessibilityService is not running.")
                callback.onCancelled(gesture)
            }
        }
    }
}