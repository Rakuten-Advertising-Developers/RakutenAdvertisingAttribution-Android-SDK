package com.rakuten.attribution.sdk.network

import com.squareup.moshi.Json

data class DeviceData(
    val os: String,
    @Json(name = "os_version") val osVersion: String,
    @Json(name = "model") val model: String,
    @Json(name = "screen_width") val screenWidth: Int,
    @Json(name = "screen_height") val screenHeight: Int,
    @Json(name = "device_id") val deviceId: String,
    @Json(name = "is_simulator") val isSimulator: Boolean
) {
    companion object {
        val default =
            DeviceData(//todo replace with real values
                os = "Android",
                osVersion = "13.89978",
                model = "emalator",
                screenWidth = 375,
                screenHeight = 812,
                deviceId = "BE6903B3-1D6B-44C1-A1D0-6E42F56AFD7",
                isSimulator = false
            )
    }
}

data class EventData(
    @Json(name = "transaction_id") val transactionId: String?,
    @Json(name = "search_query") val searchQuery: String?,
    val currency: String?,
    val revenue: Double?,
    val shipping: Double?,
    val tax: Double?,
    val coupon: String?,
    val affiliation: String?,
    val description: String?
)

data class UserData(
    @Json(name = "bundle_identifier") val applicationId: String,
    @Json(name = "app_version") val versionName: String
) {
    companion object {
        val default =
            UserData(//todo replace with real values
                applicationId = "com.rakuten.advertising.RADAttribution-Example",
                versionName = "0.0.1"
            )
    }
}

typealias CustomData = Map<String, String>