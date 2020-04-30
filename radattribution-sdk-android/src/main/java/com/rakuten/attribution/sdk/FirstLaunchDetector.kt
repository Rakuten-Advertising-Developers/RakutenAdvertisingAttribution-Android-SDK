package com.rakuten.attribution.sdk

interface FirstLaunchDetector {
    val isFirstLaunch: Boolean
}


class StubFirstLaunchDetector : FirstLaunchDetector {
    override val isFirstLaunch: Boolean
        get(){
            throw NotImplementedError()
        }
}