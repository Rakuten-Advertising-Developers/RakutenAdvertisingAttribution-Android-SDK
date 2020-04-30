package com.rakuten.attribution.sdk

object TokensStorage {
    private var internalToken: String? = null
    private var internalSessionID: String? = null

    val token: String? = internalToken
    val sessionID: String? = internalSessionID

    fun modifyToken(token: String?) {
        internalToken = token
    }

    fun modifySessionId(sessionId: String?) {
        internalSessionID = sessionId
    }
}