package com.rakuten.attribution.sdk

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirstLaunchDetectorTest {

    @Test
    fun isFirstLaunch() {
        val context = InstrumentationRegistry.getInstrumentation().context

        val fld1 = FirstLaunchDetector(context)
        fld1.clear()

        assertTrue(fld1.isFirstLaunch)
        assertFalse(fld1.isFirstLaunch)

        val fld2 = FirstLaunchDetector(context)
        assertFalse(fld2.isFirstLaunch)
    }
}