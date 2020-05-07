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

class RAdAttribution(
    context: Context,
    private val configuration: Configuration
) {
    companion object {
        val tag = RAdAttribution::class.java.simpleName
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val tokenStorage = TokensStorage()

    @VisibleForTesting
    val tokenProvider = JwtProvider(
        configuration.appId,
        configuration.privateKey,
        tokenStorage
    )

    private val sessionStorage = SessionStorage()
    private val firstLaunchDetector = FirstLaunchDetector(context)

    private val deviceData = DeviceData.create(context)
    private val userData = UserData.create(configuration.appId)

    @VisibleForTesting
    val eventSender: EventSender = EventSender(
        userData = userData,
        deviceData = deviceData,
        tokenProvider = tokenProvider,
        sessionStorage = sessionStorage,
        scope = coroutineScope
    )

    @VisibleForTesting
    val linkResolver: LinkResolver = LinkResolver(
        userData = userData,
        deviceData = deviceData,
        tokenProvider = tokenProvider,
        firstLaunchDetector = firstLaunchDetector,
        sessionStorage = sessionStorage,
        scope = coroutineScope
    )

    init {
        sendAppLaunchedEventIfNeeded()
        validate()
    }

    private fun validate(): Boolean {
        return try {
            tokenProvider.obtainToken()
            true
        } catch (e: Exception) {
            Log.e(tag, "Configuration.validate() failed: ${e.message}")
            false
        }
    }

    private fun sendAppLaunchedEventIfNeeded() {
        if (configuration.isManualAppLaunch) {
            linkResolver.resolve(link = "")
        }
    }
}