package com.rakuten.attribution.sdk.network

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val TIMEOUT = 2_00000L

class FingerprintFetcher(private val context: Context) {
    companion object {
        val TAG = FingerprintFetcher::class.java.simpleName
    }

    private val url: String = if (com.rakuten.attribution.sdk.BuildConfig.DEBUG) {
        "https://click.rakutenadvertising.io/fingerprint"
    } else {
        "https://click.staging.rakutenadvertising.io/fingerprint"
    }


    private lateinit var webView: WebView

    private val job = Job()
    private val scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    private val scopeFroTimeout = CoroutineScope(job + Dispatchers.Default)

    init {
        scopeMainThread.launch {
            webView = WebView(context)
            webView.settings.javaScriptEnabled = true
        }
    }

    suspend fun fetch(): String {
        return suspendCoroutine { continuation ->
            scopeMainThread.launch {
                webView.addJavascriptInterface(object {
                    @JavascriptInterface
                    fun postMessage(value: String?) {
                        Log.d(TAG, "postMessage() = $value")
                        scopeFroTimeout.cancel()
                        continuation.resume(value ?: "")
                    }

                }, "finger")
                webView.loadUrl(url)
//              webView.loadUrl("javascript:window.finger.postMessage(\"test data\")")// let it be
            }
            scopeFroTimeout.launch {
                delay(TIMEOUT)
                scopeMainThread.cancel()
                Log.d(TAG, "${TIMEOUT}ms timeout reached, return empty fingerPrint")
                continuation.resume("")
            }
        }
    }
}