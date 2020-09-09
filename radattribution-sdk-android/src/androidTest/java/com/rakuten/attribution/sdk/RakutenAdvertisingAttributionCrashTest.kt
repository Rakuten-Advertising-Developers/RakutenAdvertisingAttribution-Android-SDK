package com.rakuten.attribution.sdk

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RakutenAdvertisingAttributionCrashTest {
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val appVersion = "1.0"

    private val deviceId = UUID.randomUUID().toString()

    private val secretKey = context.assets
        .open("private_key")
        .bufferedReader()
        .use { it.readText() }

    private val configuration = Configuration(
        appId = context.packageName,
        appVersion = appVersion,
        privateKey = secretKey,
        endpointUrl = "https://attribution-sdk-endpoint-ff5ckcoswq-uc.a.run.app/v2/",
        deviceId = deviceId)

    @Test
    fun testExceptionWhenCalledFromWrongThread() = runBlocking {
        try {
            RakutenAdvertisingAttribution.setup(context, configuration)
        } catch (e: Exception) {
            Assert.assertEquals(IllegalStateException::class.java, e::class.java)
        }
        try {
            RakutenAdvertisingAttribution.resolve("")
        } catch (e: Exception) {
            Assert.assertEquals(IllegalStateException::class.java, e::class.java)
        }
        try {
            RakutenAdvertisingAttribution.sendEvent("")
        } catch (e: Exception) {
            Assert.assertEquals(IllegalStateException::class.java, e::class.java)
        }
    }

    @Test
    fun callFromMainThread() = runBlocking{
        withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
            RakutenAdvertisingAttribution.setup(context, configuration)
        }
    }
}