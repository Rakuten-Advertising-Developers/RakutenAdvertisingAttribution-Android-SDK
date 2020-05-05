package com.rakuten.attribution.sdk

import android.util.Log
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.*

class EventSender(
    private val tokenProvider: JwtProvider,
    private val firstLaunchDetector: FirstLaunchDetector = StubFirstLaunchDetector()
) {
    companion object {
        val tag = EventSender::class.java.simpleName
    }

    suspend fun sendEvent(name: String, eventData: EventData? = null) {
        val token = tokenProvider.obtainToken()

        val request = SendEventRequest(
            name = "",
            sessionId = null,
            userData = UserData.default,
            deviceData = DeviceData.default
        )

        val result = RAdApi.retrofitService.sendEventAsync(request, token).await()
        //todo add proper callback
        Log.d(tag, "received = $result")
    }
}