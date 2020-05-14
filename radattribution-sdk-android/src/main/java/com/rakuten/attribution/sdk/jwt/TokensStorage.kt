package com.rakuten.attribution.sdk.jwt

internal class TokensStorage {
    private var internalToken: Token? = null
    val token: Token? = internalToken

    fun saveToken(token: Token) {
        internalToken = token
    }

    fun hasValidToken(): Boolean {
        if (token == null) return false

        val now = System.currentTimeMillis()
        if (now > token.expires) {
            return false
        }
        return true
    }
}

internal data class Token(
    val value: String,
    val expires: Long
)