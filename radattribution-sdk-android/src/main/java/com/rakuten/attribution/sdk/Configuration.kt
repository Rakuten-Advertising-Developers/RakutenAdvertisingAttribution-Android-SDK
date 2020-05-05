package com.rakuten.attribution.sdk

import android.util.Log
import com.rakuten.attribution.sdk.jwt.JwtProvider

data class Configuration(
    val appId: String,
    val privateKey: String,
    val isManualAppLaunch: Boolean
) {

    companion object {
        val default = Configuration(
            appId = "",
            privateKey = "",
            isManualAppLaunch = false
        )
    }
}