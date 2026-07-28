package com.example.reelslimiter

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppState {
    private const val PREFS_NAME = "reels_prefs"
    private const val KEY_COUNT = "today_count"
    private const val KEY_DATE = "today_date"
    private const val KEY_LIMIT = "daily_limit"

    private fun todayString(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return fmt.format(Date())
    }

    private fun resetIfNewDay(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedDate = prefs.getString(KEY_DATE, "")
        if (savedDate != todayString()) {
            prefs.edit()
                .putInt(KEY_COUNT, 0)
                .putString(KEY_DATE, todayString())
                .apply()
        }
    }

    fun getCount(context: Context): Int {
        resetIfNewDay(context)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_COUNT, 0)
    }

    fun incrementCount(context: Context) {
        resetIfNewDay(context)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = prefs.getInt(KEY_COUNT, 0)
        prefs.edit().putInt(KEY_COUNT, current + 1).apply()
    }

    fun getLimit(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_LIMIT, 1000)
    }

    fun setLimit(context: Context, limit: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_LIMIT, limit).apply()
    }

    fun isLimitReached(context: Context): Boolean {
        return getCount(context) >= getLimit(context)
    }
}
