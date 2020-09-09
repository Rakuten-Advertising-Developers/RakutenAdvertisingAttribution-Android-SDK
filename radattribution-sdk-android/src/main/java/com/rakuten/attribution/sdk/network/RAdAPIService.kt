package com.rakuten.attribution.sdk.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rakuten.attribution.sdk.CustomDateAdapter
import com.rakuten.attribution.sdk.RAdDeepLinkData
import com.rakuten.attribution.sdk.RAdSendEventData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.*

//private const val BASE_URL = "https://attribution-sdk-endpoint-ff5ckcoswq-uc.a.run.app/v2/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(Date::class.javaObjectType,  CustomDateAdapter())
    .build()

private val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(logging)
}.build()

internal interface RAdAPIService {
//    @POST("resolve-link")
    @POST("resolve-link-rak")
    @Headers("Content-Type:application/json")
    fun resolveLinkAsync(
        @Body request: ResolveLinkRequest,
        @Header("Authorization") token: String
    ): Deferred<RAdDeepLinkData>

//    @POST("send-event")
    @POST("send-event-rak")
    @Headers("Content-Type:application/json")
    fun sendEventAsync(
        @Body request: SendEventRequest,
        @Header("Authorization") token: String
    ): Deferred<RAdSendEventData>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
internal object RAdApi {
    private lateinit var retrofit: Retrofit

    fun init(endpointUrl: String) {
        retrofit = Retrofit.Builder()
            .baseUrl(endpointUrl)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    val retrofitService: RAdAPIService by lazy {
        retrofit.create(
            RAdAPIService::class.java
        )
    }
}