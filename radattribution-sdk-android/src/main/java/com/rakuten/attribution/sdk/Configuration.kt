package com.rakuten.attribution.sdk

/**
 * A type that provides an ability to configure SDK
 *
 * @property appId android application id
 * @property privateKey RS256 private to sign requests
 * @property isManualAppLaunch if application opened from link with the associated domain - `false`,
 * otherwise `true`
 */
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