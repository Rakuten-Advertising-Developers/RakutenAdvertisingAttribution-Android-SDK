package com.rakuten.attribution.sdk

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.rakuten.attribution.sdk.jwt.JwtProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class RAdAttribution(
    private val configuration: Configuration,
    private val coroutineScope: CoroutineScope
) {
    companion object {
        val tag = RAdAttribution::class.java.simpleName
    }
    init {
        sendAppLaunchedEventIfNeeded()
        validate()
    }

    private val tokenStorage = TokensStorage

    @VisibleForTesting
    val tokenProvider = JwtProvider(
        configuration.appId,
        configuration.privateKey,
        tokenStorage
    )

    @VisibleForTesting
    val eventSender: EventSender = EventSender()

    @VisibleForTesting
    val linkResolver: LinkResolver = LinkResolver(tokenProvider)

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