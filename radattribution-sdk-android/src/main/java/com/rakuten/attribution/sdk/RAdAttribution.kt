package com.rakuten.attribution.sdk

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.content.Context
import android.provider.Settings.*
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.*

enum class RAdApiStatus { LOADING, ERROR, DONE }

class RAdAttribution(context: Context) {

    private val  _context = context;

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<RAdApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<RAdApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating RAdDeepLinkData
    private val _rAdDeepLinkData = MutableLiveData<RAdDeepLinkData>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val rAdDeepLinkData: LiveData<RAdDeepLinkData>
        get() = _rAdDeepLinkData


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var radJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(radJob + Dispatchers.Main)


    /**
     * Call resolveLink () on init so we can display status immediately.
     */
    init {
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), resolveLinkRequestBody().toString())
        Log.i("attributeSDK", resolveLinkRequestBody().toString() )

       resolveLink(requestBody)
    }


    /**
     *  The Retrofit service
     * returns a coroutine Deferred, which we await to get the result of the transaction.
     *
     */
    private fun resolveLink(requestBody:RequestBody) {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var resolveLinkDeffered = RAdAPI.retrofitService.resolveLink(requestBody);
            try {
                _status.value = RAdApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val rAdLinkData = resolveLinkDeffered.await()
                _status.value = RAdApiStatus.DONE

               _rAdDeepLinkData.value = rAdLinkData
            } catch (e: Exception) {
                Log.e("attributeSDK", e.localizedMessage )
                _status.value = RAdApiStatus.ERROR
            }
        }
    }


    fun sendPurchaseEvent() {
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), sendEventRequestBody().toString())
        Log.i("attributeSDK PURCHASE", sendEventRequestBody().toString() )
        sendEvent(requestBody)
    }

    /**
     *  The Retrofit service
     * returns a coroutine Deferred, which we await to get the result of the transaction.
     *
     */
    private fun sendEvent(requestBody:RequestBody) {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var sendEventDeferred = RAdAPI.retrofitService.sendEvent(requestBody);
            try {
                _status.value = RAdApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val sendEventResp = sendEventDeferred.await()
                _status.value = RAdApiStatus.DONE

                Log.i("attributeSDK", sendEventResp.toString() )


            } catch (e: Exception) {
                Log.e("attributeSDK", e.localizedMessage )
                _status.value = RAdApiStatus.ERROR
            }
        }
    }

    private fun sendEventRequestBody():JSONObject {
        val json = JSONObject()
        json.put("name", "PURCHASE")
        json.put("session_id", UUID.randomUUID().toString())
        json.put( "google_advertising_id", "a53832e3-8fc9-4b81-8e2f-bddbc2766f90")

        json.put("event_data", getEventData())
        json.put("user_data", getUserData())
        json.put("device_data", getDeviceData())
        return json

    }


    private fun resolveLinkRequestBody():JSONObject {
        val json = JSONObject()
        json.put("session_id", UUID.randomUUID().toString())
        json.put("first_session", true)
        json.put("android_app_link_url", "rakutenready://")
        json.put("universal_link_url",  "https://rakutenready.app.link/ui3knDTZH0?%243p=a_rakuten_marketing&ran_mid={ran_mid}&click_id={click_id}")
        json.put( "google_advertising_id", "a53832e3-8fc9-4b81-8e2f-bddbc2766f90")

        json.put("user_data", getUserData())
        json.put("device_data", getDeviceData())
        return json
    }


    private fun getEventData():JSONObject {
        val json = JSONObject()
        json.put("name", "PURCHASE")
        json.put("transactionId", "234")
        json.put("revenue", 88.88)
        json.put("description", "Android Test Order")
        return json

    }

    private fun getUserData(): JSONObject {
        val json = JSONObject()
        json.put("bundle_identifier", "com.rakutenadvertising.radadvertiserdemo")
        json.put("app_version", Build.VERSION.SDK_INT.toString())


        return json
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceData(): JSONObject {
        val json = JSONObject()
        json.put("os", "Android")
        json.put("os_version", Build.VERSION.SDK_INT.toString())

        json.put("device_id", Secure.getString(_context.contentResolver, Secure.ANDROID_ID))
        json.put("is_simulator", false)
        json.put( "google_advertising_id", "a53832e3-8fc9-4b81-8e2f-bddbc2766f90")
        return json
    }
}