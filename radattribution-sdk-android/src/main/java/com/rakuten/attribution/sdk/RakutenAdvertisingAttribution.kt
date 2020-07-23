package com.rakuten.attribution.sdk

import android.content.Context
import android.provider.Settings.Secure.ANDROID_ID
import android.util.Log
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.jwt.TokensStorage
import com.rakuten.attribution.sdk.network.AndroidAdIdFetcher
import com.rakuten.attribution.sdk.network.FingerprintFetcher
import com.rakuten.attribution.sdk.network.RAdApi
import kotlinx.coroutines.*

/**
An object that encapsulates various features of RAdAttribution SDK,
like sending events and links resolving
 */
object RakutenAdvertisingAttribution {
    private val TAG = RakutenAdvertisingAttribution::class.java.simpleName

    private lateinit var context: Context
    private lateinit var configuration: Configuration

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val tokenStorage = TokensStorage()

    internal lateinit var tokenProvider: JwtProvider

    internal lateinit var eventSenderInternal: Deferred<EventSender>
    internal lateinit var linkResolverInternal: Deferred<LinkResolver>

    private lateinit var sessionStorage: SessionStorage
    private lateinit var firstLaunchDetector: FirstLaunchDetector

    private lateinit var deviceData: Deferred<DeviceData>
    private lateinit var userData: UserData

    /**
     *  Setups RAdAttribution SDK.
     *  Should be called before SDK usage.
     *
     * @param context instance of Android Context class
     * @param configuration an instance of Configuration class,
     * filled with required info
     */
    fun setup(
        context: Context,
        configuration: Configuration
    ) {
        Log.i(TAG, "setup()")

        this.context = context
        this.configuration = configuration

        tokenProvider = JwtProvider(
            RakutenAdvertisingAttribution.configuration.appId,
            RakutenAdvertisingAttribution.configuration.privateKey,
            tokenStorage
        )
        sessionStorage = SessionStorage()
        firstLaunchDetector = FirstLaunchDetector(RakutenAdvertisingAttribution.context)

        userData = UserData.create(
            RakutenAdvertisingAttribution.configuration.appId,
            configuration.appVersion
        )

        RAdApi.init(endpointUrl = configuration.endpointUrl)
        validateToken()

        deviceData = obtainDeviceDataAsync()

        eventSenderInternal = coroutineScope.async {
            EventSender(
                userData = userData,
                deviceData = deviceData.await(),
                tokenProvider = tokenProvider,
                sessionStorage = sessionStorage,
                scope = coroutineScope
            )
        }

        linkResolverInternal = coroutineScope.async {
            LinkResolver(
                userData = userData,
                deviceData = deviceData.await(),
                tokenProvider = tokenProvider,
                firstLaunchDetector = firstLaunchDetector,
                sessionStorage = sessionStorage,
                scope = coroutineScope
            )
        }
    }

    /**
     * Sends event to server
     *
     * @param name event's name. i.e. "ADD_TO_CART"
     * @param eventData meta data associated with event
     * @param customData custom data associated with event
     * @param contentItems content items associated with event
     * @param callback lambda to be called with operation result
     */
    fun sendEvent(
        name: String,
        eventData: EventData? = null,
        customData: CustomData = emptyMap(),
        contentItems: Array<ContentItem> = emptyArray(),
        callback: ((Result<RAdSendEventData>) -> Unit)? = null
    ) {
        coroutineScope.launch {
            eventSenderInternal.await()
                .sendEvent(name, eventData, customData, contentItems, callback)
        }
    }

    fun resolve(link: String, callback: ((Result<RAdDeepLinkData>) -> Unit)? = null) {
        coroutineScope.launch {
            linkResolverInternal.await()
                .resolve(link, callback)
        }
    }

    private fun validateToken() {
        tokenProvider.obtainToken()
    }

    private fun obtainDeviceDataAsync(): Deferred<DeviceData> {
        return coroutineScope.async {
            val fingerPrint = GlobalScope.async {
                FingerprintFetcher(context).fetch()
            }

            val androidAdId = GlobalScope.async {
                AndroidAdIdFetcher(context).fetch()
            }
            return@async DeviceData.create(
                deviceId = configuration.deviceId ?: ANDROID_ID,
                fingerPrint = fingerPrint.await(),
                googleAdvertisingId = androidAdId.await()
            )
        }
    }
}