package com.rakuten.attribution.sdk

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rakuten.attribution.sdk.network.FingerprintFetcher
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class RakutenAdvertisingAttributionTest {
    private lateinit var context: Context
    private lateinit var appId: String
    private lateinit var appVersion: String
    private lateinit var fingerPrint: String

    private val deviceId = "368ab401-94ac-429c-956a-b2db0bfea9b3"

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        appId = "com.rakutenadvertising.radadvertiserdemo"
        appVersion = "1.0"

        val secretKey = context.assets
            .open("private_key")
            .bufferedReader()
            .use { it.readText() }

        val configuration = Configuration(
            appId = context.packageName,
            appVersion = appVersion,
            privateKey = secretKey,
            endpointUrl = "https://api.rakutenadvertising.io/v2/",
            deviceId = deviceId
        )
        runBlocking {

            fingerPrint = FingerprintFetcher(context).fetch()
            withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                RakutenAdvertisingAttribution.setup(context, configuration)
            }
        }
    }

    @Test
    fun obtainToken() {
        val token = RakutenAdvertisingAttribution.tokenProvider.obtainToken()
        assertTrue("token is not generated", token.isNotBlank())
    }

    @Test
    fun resolveEmptyLink() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdDeepLinkData>?> = CompletableDeferred()


        RakutenAdvertisingAttribution.linkResolverInternal.await().resolve(
            "",
            userData = UserData.create(appId, appVersion),
            deviceData = DeviceData.create(deviceId = deviceId, fingerPrint = fingerPrint)
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

        val request =
            RakutenAdvertisingAttribution.linkResolverInternal.await().createRequest(
                link = uri,
                userData = UserData.create(appId, appVersion),
                deviceData = DeviceData.create(deviceId)
            )

        assertEquals(request.appLinkUrl, "")
        assertEquals(request.linkIdentifier, "1234")

        RakutenAdvertisingAttribution.linkResolverInternal.await().resolve(
            uri,
            userData = UserData.create(
                appId = appId,
                appVersion = appVersion
            ),
            deviceData = DeviceData.create(
                deviceId = deviceId,
                fingerPrint = fingerPrint
            )
        ) {
            deferredResult.complete(it)
        }
        val result = deferredResult.await()
        assertTrue(result is Result.Success)

        val linkData = (result as Result.Success).data
        assertNotNull(linkData.clickTimestamp)
    }

    @Test
    fun resolveZAppLink() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdDeepLinkData>?> = CompletableDeferred()
        val link =
            "https://click.rakutenadvertising.io/dl/44066?$3p=a_custom_781200801305432596&id=lMh2Xiq9xN0&offerid=12345&type=3&subid=0&mid=44066&u1=ram"

        val request =
            RakutenAdvertisingAttribution.linkResolverInternal.await().createRequest(
                link = link,
                userData = UserData.create(appId, appVersion),
                deviceData = DeviceData.create(deviceId)
            )

        assertEquals(request.appLinkUrl, link)
        assertEquals(request.linkIdentifier, "")

        RakutenAdvertisingAttribution.linkResolverInternal.await().resolve(
            link,
            userData = UserData.create(appId, appVersion),
            deviceData = DeviceData.create(deviceId = deviceId, fingerPrint = fingerPrint)
        ) {
            deferredResult.complete(it)
        }
        val result = deferredResult.await()
        assertTrue(result is Result.Success)

        val linkData = (result as Result.Success).data
        assertNotNull(linkData.clickTimestamp)
    }

    @Test
    fun sendEvent() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdSendEventData>?> = CompletableDeferred()

        RakutenAdvertisingAttribution.eventSenderInternal.await().sendEvent(
            name = "ADD_TO_CART",
            eventData = null,
            userData = UserData.create(appId, appVersion),
            deviceData = DeviceData.create(deviceId = deviceId, fingerPrint = fingerPrint)
        ) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }

    @Test
    fun sendEventWithCustomData() = runBlocking {
        val deferredResult: CompletableDeferred<Result<RAdSendEventData>?> = CompletableDeferred()

        val item1 = ContentItem(
            sku = "sku_1",
            price = 1.99,
            productName = "name_1",
            quantity = 1
        )
        val item2 = ContentItem(
            sku = "sku_2",
            price = 2.99,
            productName = "name_2",
            quantity = 2
        )

        val request = RakutenAdvertisingAttribution.eventSenderInternal.await().createRequest(
            name = "ADD_TO_CART",
            eventData = null,
            userData = UserData.create(appId, appVersion),
            deviceData = DeviceData.create(deviceId = deviceId, fingerPrint = fingerPrint),
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

        RakutenAdvertisingAttribution.eventSenderInternal.await().sendEvent(
            name = "ADD_TO_CART",
            eventData = null,
            userData = UserData.create(appId, appVersion),
            deviceData = DeviceData.create(deviceId, fingerPrint = fingerPrint),
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
            description = "test_description",
        )
        RakutenAdvertisingAttribution.eventSenderInternal.await().sendEvent(
            name = "ADD_TO_CART",
            eventData = eventData,
            userData = UserData.create(appId, appVersion),
            deviceData = DeviceData.create(
                deviceId = deviceId,
                fingerPrint = fingerPrint
            )
        ) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()
        assertTrue(result is Result.Success)
    }
}