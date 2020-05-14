package com.rakuten.attribution.sdk

import com.squareup.moshi.Json

/**
A class that represents response on link resolving operation
 */
data class RAdDeepLinkData internal constructor(
    @Json(name = "session_id") val sessionId: String,
    @Json(name = "device_fingerprint_id") val deviceFingerprintId: String,
    var link: String = "",
    var data: Map<String, Any>
)