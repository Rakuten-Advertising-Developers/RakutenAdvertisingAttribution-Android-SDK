package com.rakuten.attribution.sdk

import com.squareup.moshi.Json

data class RAdDeepLinkData(
    @Json(name = "session_id") val sessionId: String,
    @Json(name = "device_fingerprint_id") val device_fingerprint_id: String,
    var link: String = "",
    var data: Map<String, Any>
)