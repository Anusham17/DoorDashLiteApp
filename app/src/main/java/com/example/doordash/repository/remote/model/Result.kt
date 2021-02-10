package com.example.doordash.repository.remote.model

import java.lang.Exception

/**
 * Sealed class to handle Success and failure results for store feed
 */
sealed class StoreFeedResult {
    data class Success(val storeFeed: StoreFeed?) : StoreFeedResult()
    data class Error(val exception: NetworkException) : StoreFeedResult()
}

/**
 * Sealed class to handle Success and failure results for store details
 */
sealed class StoreDetailResult {
    data class Success(val store: StoreDetails?) : StoreDetailResult()
    data class Error(val exception: NetworkException) : StoreDetailResult()
}

data class NetworkException(
    val errorMessage: String?
) : Exception()