package com.rakuten.attribution.sdk

data class ResolveLinkRequest(
    val firstSession: Boolean,
    val universalLinkUrl: String,
    val userData: UserData,
    val deviceData: DeviceData
)

data class UserData(
    val applicationId: String,
    val versionName: String
) {
    companion object {
        val default: UserData
            get() {
                throw NotImplementedError()
            }
    }
}

data class DeviceData(
    val os: String,
    val osVersion: String,
    val model: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val deviceId: String,
    val isSimulator: Boolean
) {
    companion object {
        val default: DeviceData
            get() {
                throw NotImplementedError()
            }
    }
}
