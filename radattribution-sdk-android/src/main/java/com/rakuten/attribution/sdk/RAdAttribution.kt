package com.rakuten.attribution.sdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class RAdAttribution(
    private val eventSender: EventSender = EventSender(),
    private val linkResolver: LinkResolver = LinkResolver(),
    private val coroutineScope: CoroutineScope
) {
    private var configuration = Configuration.default

    fun setup(configuration: Configuration) {
        this.configuration = configuration
    }

    private fun checkConfiguration() {

        val isValid = configuration.validate()
        assert(isValid) {
            "Provide valid AttributionConfiguration by calling " +
                    "RADAttribution.setup(with configuration:) at first"
        }
    }

    init {
        checkConfiguration()
        sendAppLaunchedEventIfNeeded()
    }


    private fun sendAppLaunchedEventIfNeeded() {
        if (!configuration.isManualAppLaunch) {
            return
        }
        coroutineScope.async {
            linkResolver.resolve(link = "")
        }
    }
}