package com.rakuten.attribution.sdk

interface FirstLaunchDetector {
    val isFirstLaunch: Boolean
}


class StubFirstLaunchDetector : FirstLaunchDetector {
    override val isFirstLaunch = true//todo implement
}