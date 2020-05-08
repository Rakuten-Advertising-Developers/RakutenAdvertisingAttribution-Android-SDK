package com.rakuten.attribution.sdk.network

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import com.rakuten.attribution.sdk.BuildConfig
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
        fun create(context: Context): DeviceData {
            val deviceId = Settings.Secure.getString(//todo: discuss
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
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

data class ContentItem(
        @Json(name = "\$sku") val sku: String?,
        @Json(name = "\$price") val price: Double?,
        @Json(name = "\$product_name") val productName: String?,
        @Json (name = "\$quantity") val quantity:Int?
)

data class UserData(
    @Json(name = "bundle_identifier") val applicationId: String,
    @Json(name = "app_version") val versionName: String,
    @Json(name = "sdk_version") val sdkVersion: String
) {
    companion object {
        fun create(appId: String): UserData {
            return UserData(
                applicationId = appId,
                versionName = BuildConfig.VERSION_NAME,
                sdkVersion = BuildConfig.VERSION_NAME
            )
        }
    }
}

typealias CustomData = Map<String, String>