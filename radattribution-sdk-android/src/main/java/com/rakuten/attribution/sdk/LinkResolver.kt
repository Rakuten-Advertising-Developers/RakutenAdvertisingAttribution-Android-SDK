package com.rakuten.attribution.sdk

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.DeviceData
import com.rakuten.attribution.sdk.network.RAdApi
import com.rakuten.attribution.sdk.network.ResolveLinkRequest
import com.rakuten.attribution.sdk.network.UserData

class LinkResolver(
    private val context: Context,
    private val tokenProvider: JwtProvider,
    private val firstLaunchDetector: FirstLaunchDetector
) {
    companion object {
        val tag = LinkResolver::class.java.simpleName
    }

    suspend fun resolve(link: String) {
        resolve(link, UserData.create(), DeviceData.create(context))
    }

    @VisibleForTesting
    suspend fun resolve(link: String, userData: UserData, deviceData: DeviceData) {
        val token = tokenProvider.obtainToken()

        val request = ResolveLinkRequest(
            firstSession = firstLaunchDetector.isFirstLaunch,
            universalLinkUrl = link,
            userData = userData,
            deviceData = deviceData
        )

        val result = RAdApi.retrofitService.resolveLinkAsync(request, token).await()
        //todo add proper callback
        Log.d(tag, "received = $result")
    }
}