package com.example.reelslimiter

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var progressRing: CircularProgressIndicator
    private lateinit var countText: TextView
    private lateinit var limitText: TextView
    private lateinit var limitInput: TextInputEditText
    private lateinit var saveButton: MaterialButton
    private lateinit var enableServiceButton: MaterialButton
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressRing = findViewById(R.id.progressRing)
        countText = findViewById(R.id.countText)
        limitText = findViewById(R.id.limitText)
        limitInput = findViewById(R.id.limitInput)
        saveButton = findViewById(R.id.saveButton)
        enableServiceButton = findViewById(R.id.enableServiceButton)
        statusText = findViewById(R.id.statusText)

        saveButton.setOnClickListener {
            val text = limitInput.text.toString()
            val newLimit = text.toIntOrNull()
            if (newLimit != null && newLimit > 0) {
                AppState.setLimit(this, newLimit)
                refreshUI()
            }
        }

        enableServiceButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        val count = AppState.getCount(this)
        val limit = AppState.getLimit(this)

        countText.text = count.toString()
        limitText.text = "of $limit reels"
        limitInput.setText(limit.toString())

        progressRing.max = limit
        progressRing.progress = if (count > limit) limit else count

        val enabled = isAccessibilityServiceEnabled()
        statusText.text = if (enabled) "Service active" else "Service not active"
        statusText.setTextColor(
            ContextCompat.getColor(this, if (enabled) R.color.success else R.color.danger)
        )
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val serviceId = "$packageName/${ReelAccessibilityService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)
        while (colonSplitter.hasNext()) {
            if (colonSplitter.next().equals(serviceId, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}

