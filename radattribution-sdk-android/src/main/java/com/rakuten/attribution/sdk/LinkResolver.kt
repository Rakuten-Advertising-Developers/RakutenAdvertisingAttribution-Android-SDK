package com.rakuten.attribution.sdk

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.DeviceData
import com.rakuten.attribution.sdk.network.RAdApi
import com.rakuten.attribution.sdk.network.ResolveLinkRequest
import com.rakuten.attribution.sdk.network.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LinkResolver(
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

    @VisibleForTesting
    fun resolve(
        link: String,
        userData: UserData,
        deviceData: DeviceData,
        callback: ((Result<RAdDeepLinkData>) -> Unit)? = null
    ) {
        val token = tokenProvider.obtainToken()

        val request = ResolveLinkRequest(
            firstSession = firstLaunchDetector.isFirstLaunch,
            universalLinkUrl = link,
            userData = userData,
            deviceData = deviceData
        )

        scope.launch {
            try {
                val result = RAdApi.retrofitService.resolveLinkAsync(request, token).await()
                val sessionId = result.sessionId

                sessionStorage.saveId(sessionId)

                Log.i(tag, "received = $sessionId")
                callback?.invoke(Result.Success(result))
            } catch (e: Exception) {
                Log.e(tag, "resolveLinkAsync failed; ${e.message}")
                callback?.invoke(Result.Error("Failed with error: ${e.message}"))
            }
        }
    }
}