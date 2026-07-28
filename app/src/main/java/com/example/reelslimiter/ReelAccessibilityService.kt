package com.example.reelslimiter

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ReelAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.packageName?.toString() != "com.instagram.android") return

        val typeString = AccessibilityEvent.eventTypeToString(event.eventType)
        val className = event.className?.toString() ?: "unknown"
        AppState.logDebug(applicationContext, "$typeString | $className")

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (!AppState.isLimitReached(applicationContext)) {
                AppState.incrementCount(applicationContext)
            }
        }
    }

    override fun onInterrupt() {}
}
