package com.rakuten.attribution.sdk

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventSender(
        private val userData: UserData,
        private val deviceData: DeviceData,
        private val tokenProvider: JwtProvider,
        private val sessionStorage: SessionStorage,
        private val scope: CoroutineScope
) {
    companion object {
        val tag = EventSender::class.java.simpleName
    }

    fun sendEvent(
            name: String,
            eventData: EventData? = null,
            customData: CustomData = emptyMap(),
            contentItems: Array<ContentItem> = emptyArray(),
            callback: ((Result<RAdSendEventData>) -> Unit)? = null
    ) {
        sendEvent(
                name = name,
                eventData = eventData,
                userData = userData,
                deviceData = deviceData,
                customData = customData,
                contentItems = contentItems,
                callback = callback
        )
    }

    @VisibleForTesting
    fun sendEvent(
            name: String,
            eventData: EventData? = null,
            userData: UserData,
            deviceData: DeviceData,
            customData: CustomData = emptyMap(),
            contentItems: Array<ContentItem> = emptyArray(),
            callback: ((Result<RAdSendEventData>) -> Unit)? = null
    ) {
        val token = tokenProvider.obtainToken()
        val request = createRequest(
                name,
                userData,
                deviceData,
                eventData,
                customData,
                contentItems
        )

        scope.launch {
            try {
                val result = RAdApi.retrofitService
                        .sendEventAsync(request, token).await()

                Log.d(tag, "received = $result")
                launch(context = Dispatchers.Main) {
                    callback?.invoke(Result.Success(result))
                }
            } catch (e: Exception) {
                Log.e(tag, "sendEventAsync failed; ${e.message}")
                launch(context = Dispatchers.Main) {
                    callback?.invoke(Result.Error("Failed with error: ${e.message}"))
                }
            }
        }
    }

    @VisibleForTesting
    internal fun createRequest(
            name: String,
            userData: UserData,
            deviceData: DeviceData,
            eventData: EventData?,
            customData: CustomData,
            contentItems: Array<ContentItem>
    ): SendEventRequest {
        val request = SendEventRequest(
                name = name,
                sessionId = sessionStorage.sessionId,
                userData = userData,
                deviceData = deviceData,
                eventData = eventData,
                customData = customData,
                contentItems = contentItems
        )
        return request
    }
}