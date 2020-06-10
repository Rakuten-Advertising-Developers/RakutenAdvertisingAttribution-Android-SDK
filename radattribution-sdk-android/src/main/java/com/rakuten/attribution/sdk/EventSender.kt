package com.rakuten.attribution.sdk

import android.util.Log
import com.rakuten.attribution.sdk.jwt.JwtProvider
import com.rakuten.attribution.sdk.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A class that can send various events via SDK
 */
class EventSender internal constructor(
    private val userData: UserData,
    private val deviceData: DeviceData,
    private val tokenProvider: JwtProvider,
    private val sessionStorage: SessionStorage,
    private val scope: CoroutineScope
) {
    companion object {
        val tag = EventSender::class.java.simpleName
    }

    /**
     * Sends event to server
     *
     * @param name event's name. i.e. "ADD_TO_CART"
     * @param eventData meta data associated with event
     * @param customData custom data associated with event
     * @param contentItems content items associated with event
     * @param callback lambda to be called with operation result
     */
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

    internal fun sendEvent(
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

    internal fun createRequest(
        name: String,
        userData: UserData,
        deviceData: DeviceData,
        eventData: EventData?,
        customData: CustomData,
        contentItems: Array<ContentItem>
    ): SendEventRequest {
        return SendEventRequest(
            name = name,
            sessionId = sessionStorage.sessionId,
            userData = userData,
            deviceData = deviceData,
            eventData = eventData,
            customData = customData,
            contentItems = contentItems
        )
    }
}