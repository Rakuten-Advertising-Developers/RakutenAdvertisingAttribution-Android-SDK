package com.rakuten.attribution.sdk

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