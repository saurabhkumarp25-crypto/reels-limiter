package com.example.reelslimiter

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ReelAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.packageName != "com.instagram.android") return

        // Log everything for now so we can study the pattern later
        Log.d("ReelsLimiter", "Event: type=${event.eventType} class=${event.className}")

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (AppState.isLimitReached(applicationContext)) {
                // Blocking logic goes here once overlay is built
                Log.d("ReelsLimiter", "LIMIT REACHED - should block now")
            } else {
                AppState.incrementCount(applicationContext)
                Log.d("ReelsLimiter", "Count now: ${AppState.getCount(applicationContext)}")
            }
        }
    }

    override fun onInterrupt() {
        Log.d("ReelsLimiter", "Service interrupted")
    }
}
