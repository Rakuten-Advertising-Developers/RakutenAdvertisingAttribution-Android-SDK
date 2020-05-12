package com.rakuten.attribution.sdk

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rakuten.attribution.sdk.network.ContentItem
import com.rakuten.attribution.sdk.network.DeviceData
import com.rakuten.attribution.sdk.network.EventData
import com.rakuten.attribution.sdk.network.UserData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RAdAttributionTest {
    private lateinit var context: Context
    private lateinit var attribution: RAdAttribution
    private lateinit var appId: String

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        appId = "com.rakutenadvertising.radadvertiserdemo"

        val secretKey = context.assets
                .open("private_key")
                .bufferedReader()
                .use { it.readText() }

        val configuration = Configuration(
                appId = context.packageName,
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
    fun resolveEmptyLink() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdDeepLinkData>?> = CompletableDeferred()

        attribution.linkResolver.resolve(
                "",
                userData = UserData.create(appId),
                deviceData = DeviceData.create(context)
        ) {
            deferredResult.complete(it)
        }
        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }

    @Test
    fun resolveUri() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdDeepLinkData>?> = CompletableDeferred()
        val uri = "test_scheme://open?link_click_id=1234"

        val request = attribution.linkResolver.createRequest(uri,
                userData = UserData.create(appId),
                deviceData = DeviceData.create(context))

        assertEquals(request.appLinkUrl, "")
        assertEquals(request.linkIdentifier, "1234")

        attribution.linkResolver.resolve(
                uri,
                userData = UserData.create(appId),
                deviceData = DeviceData.create(context)
        ) {
            deferredResult.complete(it)
        }
        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }

    @Test
    fun resolveAppLink() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdDeepLinkData>?> = CompletableDeferred()
        val link = "https://rakutenadvertising.app.link/SVOVLqKrR5?%243p=a_rakuten_marketing"

        val request = attribution.linkResolver.createRequest(link,
                userData = UserData.create(appId),
                deviceData = DeviceData.create(context))

        assertEquals(request.appLinkUrl, link)
        assertEquals(request.linkIdentifier, "")

        attribution.linkResolver.resolve(
                link,
                userData = UserData.create(appId),
                deviceData = DeviceData.create(context)
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
                                os = "iOS"//set wrong iOS name to cause an error
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
        ) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }

    @Test
    fun sendEventWithCustomData() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdSendEventData>?> = CompletableDeferred()

        val item1 = ContentItem(sku = "sku_1", price = 1.99, productName = "name_1", quantity = 1)
        val item2 = ContentItem(sku = "sku_2", price = 2.99, productName = "name_2", quantity = 2)

        val request = attribution.eventSender.createRequest(
                name = "ADD_TO_CART",
                eventData = null,
                userData = UserData.create(appId),
                deviceData = DeviceData.create(context),
                customData = mapOf("key_1" to "value_1", "key_2" to "value_2", "key_3" to "value_3"),
                contentItems = arrayOf(item1, item2)
        )

        assertEquals(2, request.contentItems.size)
        assertEquals(1.99, request.contentItems[0].price!!, 0.00001)
        assertEquals("sku_1", request.contentItems[0].sku!!)
        assertEquals("name_1", request.contentItems[0].productName!!)

        assertEquals(2.99, request.contentItems[1].price!!, 0.00001)
        assertEquals("sku_2", request.contentItems[1].sku!!)
        assertEquals("name_2", request.contentItems[1].productName!!)

        assertEquals(3, request.customData.size)
        assertEquals(request.customData["key_1"], "value_1")
        assertEquals(request.customData["key_3"], "value_3")

        attribution.eventSender.sendEvent(
                name = "ADD_TO_CART",
                eventData = null,
                userData = UserData.create(appId),
                deviceData = DeviceData.create(context),
                customData = mapOf("key_1" to "value_1", "key_2" to "value_2", "key_3" to "value_3"),
                contentItems = arrayOf(item1, item2)
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