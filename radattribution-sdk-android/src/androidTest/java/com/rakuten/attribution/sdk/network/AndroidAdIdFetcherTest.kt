package com.rakuten.attribution.sdk.network

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch

class AndroidAdIdFetcherTest {
    private lateinit var fetcher: AndroidAdIdFetcher

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        fetcher = AndroidAdIdFetcher(context)
    }

    @Test(timeout = 2_000)
    fun fetch() {
        val countDownLatch = CountDownLatch(1)
        var aaid = ""

        GlobalScope.launch {
            aaid = fetcher.fetch()
            countDownLatch.countDown()
        }
        countDownLatch.await()

        assertFalse(aaid.isEmpty())
    }
}