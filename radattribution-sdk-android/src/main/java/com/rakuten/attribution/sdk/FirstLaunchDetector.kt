package com.rakuten.attribution.sdk

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.VisibleForTesting

internal class FirstLaunchDetector(context: Context) {
    companion object {
        const val NAME = "first_launch_prefs"
    }

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(NAME, MODE_PRIVATE)

    val isFirstLaunch: Boolean
        get() {
            val firstLaunch = sharedPreferences.getBoolean(NAME, true)
            sharedPreferences.edit().putBoolean(NAME, false).apply()
            return firstLaunch
        }

    @VisibleForTesting
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}