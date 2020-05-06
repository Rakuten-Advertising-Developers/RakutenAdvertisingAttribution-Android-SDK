package com.rakuten.attribution.sdk

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.*

class EventSender(
    private val context: Context,
    private val tokenProvider: JwtProvider,
    private val sessionStorage: SessionStorage
) {
    companion object {
        val tag = EventSender::class.java.simpleName
    }

    suspend fun sendEvent(name: String, eventData: EventData? = null) {
        sendEvent(name, eventData, UserData.create(), DeviceData.create(context))
    }

    @VisibleForTesting
    suspend fun sendEvent(
        name: String,
        eventData: EventData? = null,
        userData: UserData,
        deviceData: DeviceData
    ): Result<RAdSendEventData> {
        val token = tokenProvider.obtainToken()
        val request = SendEventRequest(
            name = name,
            sessionId = sessionStorage.sessionId,
            userData = userData,
            deviceData = deviceData,
            eventData = eventData
        )

        return try {
            val result = RAdApi.retrofitService
                .sendEventAsync(request, token).await()

            Log.d(tag, "received = $result")
            Result.Success(result)
        } catch (e: Exception) {
            Log.e(tag, "sendEventAsync failed; ${e.message}")
            Result.Error("Failed with error: ${e.message}")
        }
    }
}