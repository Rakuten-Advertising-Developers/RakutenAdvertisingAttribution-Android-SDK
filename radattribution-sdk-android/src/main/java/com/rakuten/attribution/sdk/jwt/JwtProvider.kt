package com.rakuten.attribution.sdk.jwt

import android.util.Base64
import com.rakuten.attribution.sdk.TokensStorage
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

class JwtProvider(
    private val appId: String,
    private val secretKey: String,
    private val tokenStorage: TokensStorage
) {
    companion object {
        const val ISSUER = "attribution-sdk"
        const val AUDIENCE = "1"
        const val TOKEN_TTL_HOURS = 24L
    }

    fun obtainToken(): String {


        val bytes = Base64.decode(secretKey, Base64.DEFAULT)

        val keySpec = PKCS8EncodedKeySpec(bytes)
        val kf = KeyFactory.getInstance("RSA")
        val key = kf.generatePrivate(keySpec)

        val now = System.currentTimeMillis()
        val expires = System.currentTimeMillis() + TOKEN_TTL_HOURS * 60 * 60 * 1000

        return Jwts.builder()
            .setIssuer(ISSUER)
            .setSubject(ISSUER)
            .setAudience(AUDIENCE)
            .setId(appId)
            .claim("iat", now)
            .claim("exp", expires)
            .signWith(key, SignatureAlgorithm.RS256)
            .compact()
    }
}