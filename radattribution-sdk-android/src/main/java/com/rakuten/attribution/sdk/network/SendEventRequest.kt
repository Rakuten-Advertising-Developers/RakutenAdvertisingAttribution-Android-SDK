package com.rakuten.attribution.sdk.network

import com.rakuten.attribution.sdk.*
import com.squareup.moshi.Json

internal class SendEventRequest(
    val name: String,
    @Json(name = "session_id") val sessionId: String?,
    @Json(name = "user_data") val userData: UserData,
    @Json(name = "device_data") val deviceData: DeviceData,
    @Json(name = "event_data") val eventData: EventData? = null,
    @Json(name = "custom_data") val customData: CustomData,
    @Json(name = "content_items") val contentItems: Array<ContentItem>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SendEventRequest

        if (name != other.name) return false
        if (sessionId != other.sessionId) return false
        if (userData != other.userData) return false
        if (deviceData != other.deviceData) return false
        if (eventData != other.eventData) return false
        if (customData != other.customData) return false
        if (!contentItems.contentEquals(other.contentItems)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (sessionId?.hashCode() ?: 0)
        result = 31 * result + userData.hashCode()
        result = 31 * result + deviceData.hashCode()
        result = 31 * result + (eventData?.hashCode() ?: 0)
        result = 31 * result + customData.hashCode()
        result = 31 * result + contentItems.contentHashCode()
        return result
    }
}

