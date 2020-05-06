package com.rakuten.attribution.sdk

data class RAdDeepLinkData(
    val session_id: String,
    val device_fingerprint_id: String,
    var link: String = "",
    var data: Map<String, Any>
)