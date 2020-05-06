package com.rakuten.attribution.sdk

class SessionStorage {
    private var internalSessionId: String? = null
    val sessionId: String? = internalSessionId

    fun saveId(token: String) {
        internalSessionId = token
    }
}