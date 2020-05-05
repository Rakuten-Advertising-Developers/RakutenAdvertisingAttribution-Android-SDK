package com.rakuten.attribution.sdk.network

import com.squareup.moshi.Json

data class SendEventRequest(
    val name: String,
    @Json(name = "session_id") val sessionId: String?,
    @Json(name = "user_data") val userData: UserData,
    @Json(name = "device_data") val deviceData: DeviceData,
    @Json(name = "custom_data") val customData: CustomData? = mapOf(),
    @Json(name = "event_data") val eventData: EventData? = null
)

