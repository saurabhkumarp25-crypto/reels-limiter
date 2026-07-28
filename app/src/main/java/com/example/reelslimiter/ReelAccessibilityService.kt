package com.example.reelslimiter

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ReelAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val pkg = event.packageName?.toString() ?: "unknown"
        val typeString = AccessibilityEvent.eventTypeToString(event.eventType)
        AppState.logDebug(applicationContext, "$pkg | $typeString")

        if (pkg == "com.instagram.android" && event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (!AppState.isLimitReached(applicationContext)) {
                AppState.incrementCount(applicationContext)
            }
        }
    }

    override fun onInterrupt() {}
}
