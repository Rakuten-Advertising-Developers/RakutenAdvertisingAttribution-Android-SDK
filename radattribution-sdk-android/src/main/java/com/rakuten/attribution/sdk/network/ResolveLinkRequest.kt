package com.rakuten.attribution.sdk.network

import com.squareup.moshi.Json

internal data class ResolveLinkRequest(
    @Json(name = "first_session") val firstSession: Boolean,
    @Json(name = "universal_link_url") val universalLinkUrl: String,
    @Json(name = "user_data") val userData: UserData,
    @Json(name = "device_data") val deviceData: DeviceData
)

