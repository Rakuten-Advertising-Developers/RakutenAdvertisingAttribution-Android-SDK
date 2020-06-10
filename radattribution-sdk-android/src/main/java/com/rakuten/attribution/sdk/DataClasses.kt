package com.rakuten.attribution.sdk

import android.content.res.Resources
import android.os.Build
import com.squareup.moshi.Json

/**
 * A class that represents info about user's device
 */
internal data class DeviceData(
        val os: String,
        @Json(name = "os_version") val osVersion: String,
        @Json(name = "model") val model: String,
        @Json(name = "screen_width") val screenWidth: Int,
        @Json(name = "screen_height") val screenHeight: Int,
        @Json(name = "device_id") val deviceId: String,
        @Json(name = "is_simulator") val isSimulator: Boolean
) {
    companion object {
        /**
         * Creates DeviceData class from Android's Context instance
         *
         * @param deviceId unique device identifier
         * @return
         */
        fun create(deviceId: String): DeviceData {
            return DeviceData(
                    os = "Android",
                    osVersion = Build.VERSION.RELEASE,
                    model = Build.MODEL,
                    screenWidth = Resources.getSystem().displayMetrics.widthPixels,
                    screenHeight = Resources.getSystem().displayMetrics.heightPixels,
                    deviceId = deviceId,
                    isSimulator = Build.FINGERPRINT.contains("generic")
            )
        }
    }
}

/**
 * A class that represents details of event data
 */
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


/**
 * A class that represents purchased item info
 */
data class ContentItem(
        @Json(name = "\$sku") val sku: String?,
        @Json(name = "\$price") val price: Double?,
        @Json(name = "\$product_name") val productName: String?,
        @Json(name = "\$quantity") val quantity: Int?
)

/**
 * A class that represents user specific data
 */
data class UserData(
        @Json(name = "bundle_identifier") val applicationId: String,
        @Json(name = "app_version") val applicationVersion: String,
        @Json(name = "sdk_version") val sdkVersion: String
) {
    companion object {
        fun create(appId: String, appVersion: String): UserData {
            return UserData(
                    applicationId = appId,
                    applicationVersion = appVersion,
                    sdkVersion = BuildConfig.SDK_VERSION
            )
        }
    }
}

/**
 * A dictionary to put custom data associated with event
 */
typealias CustomData = Map<String, String>