package com.rakuten.attribution.sdk

import android.net.Uri
import android.util.Log
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.RAdApi
import com.rakuten.attribution.sdk.network.ResolveLinkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A class that can resolve links via SDK
 */
class LinkResolver internal constructor(
        private val userData: UserData,
        private val deviceData: DeviceData,
        private val tokenProvider: JwtProvider,
        private val firstLaunchDetector: FirstLaunchDetector,
        private val sessionStorage: SessionStorage,
        private val scope: CoroutineScope
) {
    companion object {
        val tag = LinkResolver::class.java.simpleName
    }

    fun resolve(link: String, callback: ((Result<RAdDeepLinkData>) -> Unit)? = null) {
        resolve(link, userData, deviceData, callback)
    }

    internal fun resolve(
            link: String,
            userData: UserData,
            deviceData: DeviceData,
            callback: ((Result<RAdDeepLinkData>) -> Unit)? = null
    ) {
        val token = tokenProvider.obtainToken()
        val request = createRequest(link, userData, deviceData)

        scope.launch {
            try {
                val result = RAdApi.retrofitService.resolveLinkAsync(request, token).await()
                val sessionId = result.sessionId

                sessionStorage.saveId(sessionId)
                Log.i(tag, "received = $sessionId")

                launch(context = Dispatchers.Main) {
                    callback?.invoke(Result.Success(result))
                }
            } catch (e: Exception) {
                Log.e(tag, "resolveLinkAsync failed; ${e.message}")

                launch(context = Dispatchers.Main) {
                    callback?.invoke(Result.Error("Failed with error: ${e.message}"))
                }
            }
        }
    }

    internal fun createRequest(
            link: String,
            userData: UserData,
            deviceData: DeviceData
    ): ResolveLinkRequest {
        val uri = Uri.parse(link)

        return when (uri.scheme) {
            "http", "https" -> ResolveLinkRequest(
                    firstSession = firstLaunchDetector.isFirstLaunch,
                    appLinkUrl = link,
                    userData = userData,
                    deviceData = deviceData
            )
            else -> ResolveLinkRequest(
                    firstSession = firstLaunchDetector.isFirstLaunch,
                    linkIdentifier = uri.getQueryParameter("link_click_id") ?: "",
                    userData = userData,
                    deviceData = deviceData
            )
        }
    }
}