package com.rakuten.attribution.sdk

class LinkResolver(
    private val firstLaunchDetector: FirstLaunchDetector = StubFirstLaunchDetector()
) {
     fun resolve(link: String) {
        val request = ResolveLinkRequest(
            firstSession = firstLaunchDetector.isFirstLaunch,
            universalLinkUrl = link,
            userData = UserData.default,
            deviceData = DeviceData.default
        )

        throw NotImplementedError()
    }
}