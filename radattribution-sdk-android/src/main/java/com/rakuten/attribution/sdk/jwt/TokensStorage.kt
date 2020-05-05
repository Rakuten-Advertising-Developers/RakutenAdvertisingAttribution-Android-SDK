package com.rakuten.attribution.sdk.jwt

object TokensStorage {
    private var internalToken: String? = null
    private var internalSessionID: String? = null

    val token: String? =
        internalToken
    val sessionId: String? =
        internalSessionID

    fun modifyToken(token: String?) {
        internalToken = token
    }

    fun modifySessionId(sessionId: String?) {
        internalSessionID = sessionId
    }
}