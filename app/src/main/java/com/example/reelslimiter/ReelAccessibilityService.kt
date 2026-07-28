package com.example.reelslimiter

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ReelAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        AppState.logDebug(applicationContext, "★★★ SERVICE CONNECTED ★★★")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val pkg = event.packageName?.toString() ?: "unknown"
        val typeString = AccessibilityEvent.eventTypeToString(event.eventType)
        AppState.logDebug(applicationContext, "$pkg | $typeString")
    }

    override fun onInterrupt() {}
}