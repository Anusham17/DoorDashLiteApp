package com.example.doordash.repository.remote.api

import com.example.doordash.repository.remote.model.StoreDetails
import com.example.doordash.repository.remote.model.StoreFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoorDashService {
    @GET("v1/store_feed")
    fun getStoreFeed(
        @Query("lat") latitude: Float,
        @Query("lng") longitude: Float,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<StoreFeed>


    @GET("/v2/restaurant/{id}")
    fun getStoreDetails(
        @Path("id") id: Int
    ): Call<StoreDetails>
}