package com.rakuten.attribution.sdk

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.jwt.TokensStorage
import com.rakuten.attribution.sdk.network.DeviceData
import com.rakuten.attribution.sdk.network.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object RAdAttribution {
    private val TAG = RAdAttribution::class.java.simpleName
    private const val ERROR = "You should call RAdAttribution.setup() before"

    lateinit var context: Context
    private lateinit var configuration: Configuration

    fun setup(
            context: Context,
            configuration: Configuration) {
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

    @VisibleForTesting
    lateinit var tokenProvider: JwtProvider

    private lateinit var sessionStorage: SessionStorage
    private lateinit var firstLaunchDetector: FirstLaunchDetector

    private lateinit var deviceData: DeviceData
    private lateinit var userData: UserData

    private lateinit var eventSenderInternal: EventSender
    val eventSender: EventSender
        get() = if (::eventSenderInternal.isInitialized) {
            eventSenderInternal
        } else {
            throw IllegalStateException(ERROR)
        }

    private lateinit var linkResolverInternal: LinkResolver
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