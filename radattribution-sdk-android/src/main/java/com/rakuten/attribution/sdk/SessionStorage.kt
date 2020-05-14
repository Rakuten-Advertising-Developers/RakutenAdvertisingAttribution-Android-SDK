package com.rakuten.attribution.sdk

internal class SessionStorage {
    private var internalSessionId: String? = null
    val sessionId: String? = internalSessionId

    fun saveId(token: String) {
        internalSessionId = token
    }
}