package com.rakuten.attribution.sdk

/**
 * A type that provides an ability to configure SDK
 *
 * @property appId android application id
 * @property privateKey RS256 private to sign requests
 * @property isManualAppLaunch if application opened from link with the associated domain - `false`, otherwise `true`
 * @property endpointUrl url which sdk will send analytics to
 * @property deviceId unique device identifier, empty string will be sent if not set
 */
data class Configuration(
    val appId: String,
    val appVersion: String,
    val privateKey: String,
    val isManualAppLaunch: Boolean,
    val endpointUrl: String,
    val deviceId: String = ""
)