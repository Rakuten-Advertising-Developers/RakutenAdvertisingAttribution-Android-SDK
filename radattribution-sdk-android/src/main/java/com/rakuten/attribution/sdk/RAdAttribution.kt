package com.rakuten.attribution.sdk

import android.content.Context
import android.util.Log
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.jwt.TokensStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
An object that encapsulates various features of RAdAttribution SDK,
like sending events and links resolving
 */
object RAdAttribution {
    private val TAG = RAdAttribution::class.java.simpleName
    private const val ERROR = "You should call RAdAttribution.setup() before"

    private lateinit var context: Context
    private lateinit var configuration: Configuration

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
            RAdAttribution.configuration.appId,
            RAdAttribution.configuration.privateKey,
            tokenStorage
        )
        sessionStorage = SessionStorage()
        firstLaunchDetector = FirstLaunchDetector(RAdAttribution.context)

        deviceData = DeviceData.create(RAdAttribution.context)
        userData = UserData.create(RAdAttribution.configuration.appId)

        //* instance of EventSender class with the ability to send events
        eventSenderInternal = EventSender(
            userData = userData,
            deviceData = deviceData,
            tokenProvider = tokenProvider,
            sessionStorage = sessionStorage,
            scope = coroutineScope
        )

        linkResolverInternal = LinkResolver(
            userData = userData,
            deviceData = deviceData,
            tokenProvider = tokenProvider,
            firstLaunchDetector = firstLaunchDetector,
            sessionStorage = sessionStorage,
            scope = coroutineScope
        )

        sendAppLaunchedEventIfNeeded()
        validate()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val tokenStorage = TokensStorage()

    internal lateinit var tokenProvider: JwtProvider

    private lateinit var sessionStorage: SessionStorage
    private lateinit var firstLaunchDetector: FirstLaunchDetector

    private lateinit var deviceData: DeviceData
    private lateinit var userData: UserData

    private lateinit var eventSenderInternal: EventSender

    /** instance of EventSender class with the ability to send events */
    val eventSender: EventSender
        get() = if (::eventSenderInternal.isInitialized) {
            eventSenderInternal
        } else {
            throw IllegalStateException(ERROR)
        }

    private lateinit var linkResolverInternal: LinkResolver

    /** instance of LinkResolver class with the ability to resolve links */
    val linkResolver: LinkResolver
        get() = if (::linkResolverInternal.isInitialized) {
            linkResolverInternal
        } else {
            throw IllegalStateException(ERROR)
        }

    private fun validate() {
        tokenProvider.obtainToken()
    }

    private fun sendAppLaunchedEventIfNeeded() {
        if (configuration.isManualAppLaunch) {
            Log.i(TAG, "send app launched event")
            linkResolverInternal.resolve(link = "")
        }
    }
}