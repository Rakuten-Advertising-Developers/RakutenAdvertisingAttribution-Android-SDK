package com.rakuten.attribution.sdk

import android.util.Log
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.DeviceData
import com.rakuten.attribution.sdk.network.RAdApi
import com.rakuten.attribution.sdk.network.ResolveLinkRequest
import com.rakuten.attribution.sdk.network.UserData

class LinkResolver(
    private val tokenProvider: JwtProvider,
    private val firstLaunchDetector: FirstLaunchDetector = StubFirstLaunchDetector()
) {
    companion object {
        val tag = LinkResolver::class.java.simpleName
    }

    suspend fun resolve(link: String) {
        val token = tokenProvider.obtainToken()

        val request = ResolveLinkRequest(
            firstSession = firstLaunchDetector.isFirstLaunch,
            universalLinkUrl = link,
            userData = UserData.default,
            deviceData = DeviceData.default
        )

        val result = RAdApi.retrofitService.resolveLinkAsync(request, token).await()
        //todo add proper callback
        Log.d(tag, "received = $result")
    }
}