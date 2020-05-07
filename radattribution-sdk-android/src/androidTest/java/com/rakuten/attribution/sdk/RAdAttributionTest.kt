package com.rakuten.attribution.sdk

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rakuten.attribution.sdk.network.DeviceData
import com.rakuten.attribution.sdk.network.EventData
import com.rakuten.attribution.sdk.network.UserData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RAdAttributionTest {
    private lateinit var context: Context
    private lateinit var attribution: RAdAttribution

    private val appId = "com.rakutenadvertising.RADAdvertiserDemo"

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context

        val secretKey = context.assets
            .open("private_key")
            .bufferedReader()
            .use { it.readText() }

        val configuration = Configuration(
            appId = appId,
            privateKey = secretKey,
            isManualAppLaunch = false
        )
        attribution = RAdAttribution(context, configuration)
    }

    @Test
    fun obtainToken() {
        val token = attribution.tokenProvider.obtainToken()
        assertTrue("token is not generated", token.isNotBlank())
    }

    @Test
    fun resolveLink() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdDeepLinkData>?> = CompletableDeferred()

        attribution.linkResolver.resolve(
            "",
            userData = UserData.create(appId),
            deviceData = DeviceData.create(context)
                .copy(
                    os = "iOS",
                    deviceId = "00000000-0000-0000-0000-000000000000",
                    osVersion = "10.0"
                )
        ) {
            deferredResult.complete(it)
        }
        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }

    @Test
    fun resolveLinkFail() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdDeepLinkData>?> = CompletableDeferred()

        attribution.linkResolver.resolve(
            "",
            userData = UserData.create(appId),
            deviceData = DeviceData.create(context)
                .copy(
                    os = "iOS",
                    deviceId = "00000000-0000-0000-0000-000000000000",
                    osVersion = "10"//os version without "." causes an error for now
                )
        ) {
            deferredResult.complete(it)
        }
        val result = deferredResult.await()
        assertTrue(result is Result.Error)
    }

    @Test
    fun sendEvent() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdSendEventData>?> = CompletableDeferred()

        attribution.eventSender.sendEvent(
            name = "ADD_TO_CART",
            eventData = null,
            userData = UserData.create(appId),
            deviceData = DeviceData.create(context)
                .copy(os = "iOS")
        ) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }

    @Test
    fun sendEventWithCustomData() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdSendEventData>?> = CompletableDeferred()

        attribution.eventSender.sendEvent(
            name = "ADD_TO_CART",
            eventData = null,
            userData = UserData.create(appId),
            deviceData = DeviceData.create(context)
                .copy(os = "iOS"),
            customData = mapOf("key_1" to "value_1", "key_2" to "value_2", "key_3" to "value_3"),
            customItems = arrayOf("item_1", "item_2", "item_3", "item_4", "item_5")
        ) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }

    @Test
    fun sendEventWithEventData() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdSendEventData>?> = CompletableDeferred()

        val eventData = EventData(
            transactionId = "123",
            searchQuery = "test_query",
            currency = "USD",
            revenue = 0.5,
            shipping = 0.6,
            tax = 0.7,
            coupon = "test_coupon",
            affiliation = "test_affiliation",
            description = "test_description"
        )
        attribution.eventSender.sendEvent(
            name = "ADD_TO_CART",
            eventData = eventData,
            userData = UserData.create(appId),
            deviceData = DeviceData.create(context)
                .copy(os = "iOS")
        ) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }
}