package com.rakuten.attribution.sdk.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rakuten.attribution.sdk.DeviceData
import com.rakuten.attribution.sdk.UserData
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DataClassesTest {
    @Test
    fun createDeviceData() {
        val deviceData = DeviceData.create(UUID.randomUUID().toString())

        assertNotNull(deviceData)
        assertNotNull(deviceData.os)
        assertNotNull(deviceData.osVersion)
        assertNotNull(deviceData.model)

        assertTrue(deviceData.screenWidth > 0)
        assertTrue(deviceData.screenHeight > 0)

        assertNotNull(deviceData.deviceId)
        assertNotNull(deviceData.isSimulator)
    }

    @Test
    fun createUserData() {
        val userData = UserData.create(appId = "appId", appVersion = "1.0")

        assertNotNull(userData)
        assertNotNull(userData.applicationId)
        assertNotNull(userData.sdkVersion)
        assertNotNull(userData.applicationVersion)
    }
}