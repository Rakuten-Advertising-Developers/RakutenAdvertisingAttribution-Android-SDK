package com.rakuten.attribution.sdk

/**
 * A type that provides an ability to configure SDK
 *
 * @property appId android application id
 * @property privateKey RS256 private to sign requests
 * @property endpointUrl url which sdk will send analytics to
 * @property deviceId unique device identifier, empty string will be sent if not set
 */
data class Configuration(
    val appId: String,
    val appVersion: String,
    val privateKey: String,
    val endpointUrl: String
)