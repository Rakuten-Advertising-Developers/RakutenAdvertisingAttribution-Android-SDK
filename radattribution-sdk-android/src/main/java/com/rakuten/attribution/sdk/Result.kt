package com.rakuten.attribution.sdk


sealed class Result<out T> {
    class Success<T>(val data: T) : Result<T>()
    class Error(val message: String) : Result<Nothing>()

    inline fun dealWithError(onError: (Error) -> Nothing): T {
        when (this) {
            is Success -> return data
            is Error -> {
                onError(this)
            }
        }
    }
}