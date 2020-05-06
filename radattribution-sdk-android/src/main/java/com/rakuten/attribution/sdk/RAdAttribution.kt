package com.rakuten.attribution.sdk

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.jwt.TokensStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class RAdAttribution(
    context: Context,
    private val configuration: Configuration
) {

    companion object {
        val tag = RAdAttribution::class.java.simpleName
    }

    init {
        sendAppLaunchedEventIfNeeded()
//        validate()//todo fix validation process
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val tokenStorage = TokensStorage()

    @VisibleForTesting
    val tokenProvider = JwtProvider(
        configuration.appId,
        configuration.privateKey,
        tokenStorage
    )

    private val firstLaunchDetector = FirstLaunchDetector(context)

    @VisibleForTesting
    val eventSender: EventSender = EventSender(tokenProvider, firstLaunchDetector)

    @VisibleForTesting
    val linkResolver: LinkResolver = LinkResolver(tokenProvider, firstLaunchDetector)

    private fun validate(): Boolean {
        return try {
            tokenProvider.obtainToken()
            true
        } catch (e: Exception) {
            //assertionFailure(error.localizedDescription)
            Log.e(tag, "Configuration.validate() failed: ${e.message}")
            false
        }
    }

    private fun sendAppLaunchedEventIfNeeded() {
        if (!configuration.isManualAppLaunch) {
            return
        }
        coroutineScope.async {
            linkResolver.resolve(link = "")
        }
    }
}