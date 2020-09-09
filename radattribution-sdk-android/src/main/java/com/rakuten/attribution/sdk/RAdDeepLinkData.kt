package com.rakuten.attribution.sdk

import com.squareup.moshi.Json
import java.util.*

/**
A class that represents response on link resolving operation
 */
data class RAdDeepLinkData internal constructor(
    @Json(name = "session_id") val sessionId: String,
    @Json(name = "device_fingerprint_id") val deviceFingerprintId: String,
    @Json(name = "click_timestamp") val clickTimestamp: String,
    var link: String = "",
    var data: Map<String, String>?
)