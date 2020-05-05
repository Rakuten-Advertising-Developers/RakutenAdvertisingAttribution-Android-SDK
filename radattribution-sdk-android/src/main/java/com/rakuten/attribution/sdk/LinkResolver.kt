package com.rakuten.attribution.sdk

import com.rakuten.attribution.sdk.jwt.JwtProvider

class LinkResolver(
    private val tokenProvider: JwtProvider,
    private val firstLaunchDetector: FirstLaunchDetector = StubFirstLaunchDetector()
) {
    fun resolve(link: String) {
        val token = tokenProvider.obtainToken()

        val request = ResolveLinkRequest(
            firstSession = firstLaunchDetector.isFirstLaunch,
            universalLinkUrl = link,
            userData = UserData.default,
            deviceData = DeviceData.default
        )
    }
}