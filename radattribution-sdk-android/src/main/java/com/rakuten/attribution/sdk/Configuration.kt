package com.rakuten.attribution.sdk

data class Configuration(
    val privateKey: PrivateKey,
    val isManualAppLaunch: Boolean
) {
    private val accessKeyProcessor = JWTHandler()
    private val tokenStorage = TokensStorage

    fun validate(): Boolean {
        return try {
            accessKeyProcessor.process(privateKey, tokenStorage)
            true
        } catch (e: Exception) {
            //assertionFailure(error.localizedDescription)
            false
        }
    }

    companion object {
        val default = Configuration(
            privateKey = PrivateKey(""),
            isManualAppLaunch = false
        )
    }
}