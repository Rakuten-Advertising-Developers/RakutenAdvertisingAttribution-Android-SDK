package com.rakuten.attribution.sdk.network

import com.rakuten.attribution.sdk.DeviceData
import com.rakuten.attribution.sdk.UserData
import com.squareup.moshi.Json

internal data class ResolveLinkRequest(
    @Json(name = "first_session") val firstSession: Boolean,
    @Json(name = "link_identifier") val linkIdentifier: String = "",
    @Json(name = "android_app_link_ url") val appLinkUrl: String = "",
    @Json(name = "user_data") val userData: UserData,
    @Json(name = "device_data") val deviceData: DeviceData
)

