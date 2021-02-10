package com.example.doordash.repository

import com.example.doordash.repository.remote.api.DoorDashService
import com.example.doordash.repository.remote.model.NetworkException
import com.example.doordash.repository.remote.model.StoreFeedResult
import com.example.doordash.repository.remote.model.StoreDetailResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DoorDashRepository {
    suspend fun getStoreFeed(
        offset: Int,
        limit: Int,
        latitude: Float,
        longitude: Float
    ): StoreFeedResult

    suspend fun getStoreDetails(
        id: Int
    ): StoreDetailResult
}

class DoorDashRepositoryImpl @Inject constructor(private val doorDashService: DoorDashService) :
    DoorDashRepository {

    /**
     * Fetches store feed from the network and wraps it in a result and returns the result
     */
    override suspend fun getStoreFeed(
        offset: Int,
        limit: Int,
        latitude: Float,
        longitude: Float
    ): StoreFeedResult {
        return withContext(Dispatchers.IO) {
            val response =
                doorDashService.getStoreFeed(latitude, longitude, offset, limit).execute()
            if (response.isSuccessful) {
                StoreFeedResult.Success(response.body())
            } else {
                StoreFeedResult.Error(NetworkException(response.errorBody()?.string()))
            }
        }

    }

    /**
     * Fetches store details from the network and wraps it in a result and returns the result
     */
   override suspend fun getStoreDetails(
        id: Int
    ): StoreDetailResult {
        return withContext(Dispatchers.IO) {
            val response = doorDashService.getStoreDetails(id).execute()
            if (response.isSuccessful) {
                StoreDetailResult.Success(response.body())
            } else {
                StoreDetailResult.Error(NetworkException(response.errorBody()?.string()))
            }
        }

    }
}