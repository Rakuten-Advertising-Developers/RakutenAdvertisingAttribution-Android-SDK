package com.rakuten.attribution.sdk

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "https://us-central1-attribution-sdk.cloudfunctions.net/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()


interface RAdAPIService {
    @POST("resolve-link") fun resolveLink(@Body request: RequestBody):
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
             Deferred<RAdDeepLinkData>

    @POST("send-event") fun sendEvent(@Body request: RequestBody):
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<RAdSendEventData>
}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object RAdAPI {
    val retrofitService : RAdAPIService by lazy { retrofit.create(RAdAPIService::class.java) }
}