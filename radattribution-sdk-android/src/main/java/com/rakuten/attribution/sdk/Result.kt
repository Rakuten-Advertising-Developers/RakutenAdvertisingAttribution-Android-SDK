package com.rakuten.attribution.sdk


/**
 * Container to return result of asynchronous methods
 *
 * @param T type of result of asynchronous operation
 */
sealed class Result<out T> {
    class Success<T>(val data: T) : Result<T>()
    class Error(val message: String) : Result<Nothing>()

    /**
     * util method to handle errors
     *
     * @param onError lambda to be called with Error instance if operation fails
     * @return operation result data in case of success
     */
    inline fun dealWithError(onError: (Error) -> Nothing): T {
        when (this) {
            is Success -> return data
            is Error -> {
                onError(this)
            }
        }
    }
}