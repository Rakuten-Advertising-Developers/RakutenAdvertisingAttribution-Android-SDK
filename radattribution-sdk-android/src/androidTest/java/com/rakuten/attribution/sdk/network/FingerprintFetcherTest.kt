package com.rakuten.attribution.sdk.network

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch

class FingerprintFetcherTest {
    private lateinit var fetcher: FingerprintFetcher

    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val context = InstrumentationRegistry.getInstrumentation().context
            fetcher = FingerprintFetcher(context)
        }
    }

    @Test
    fun fetchFingerPrint() {
        val countDownLatch = CountDownLatch(1)
        var fingerprint = ""

        GlobalScope.launch {
            fingerprint = fetcher.fetch()
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertTrue(fingerprint.isNotEmpty())
    }
}