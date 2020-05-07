package com.rakuten.attribution.sdk.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataClassesTest {
    @Test
    fun createDeviceData() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val deviceData = DeviceData.create(context)

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
        val userData = UserData.create("appId")

        assertNotNull(userData)
        assertNotNull(userData.applicationId)
        assertNotNull(userData.sdkVersion)
        assertNotNull(userData.versionName)
    }
}