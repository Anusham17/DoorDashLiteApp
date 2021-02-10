package com.example.doordash.repository

import com.example.doordash.repository.remote.api.DoorDashService
import com.example.doordash.repository.remote.model.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Response

class DoorDashRepositoryTest {

    private lateinit var repository: DoorDashRepository

    private var doorDashService = Mockito.mock(DoorDashService::class.java)

    @Before
    fun setUp() {
        repository = DoorDashRepositoryImpl(doorDashService)
    }

    @Test
    fun `fetch store feed success`() = runBlocking {
        val store = Store(
            30,
            "Thailand",
            "Coconout soup",
            null,
            null
        )
        val responseList = listOf(store)
        Mockito.`when`(
            doorDashService.getStoreFeed(
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(
            Mockito.mock(Call::class.java) as Call<StoreFeed>
        )

        val call = doorDashService.getStoreFeed(0f, 0f, 1, 1)

        Mockito.`when`(call.execute()).thenReturn(
            Response.success(
                StoreFeed(
                    size = 40,
                    isFirstTimeUser = false,
                    sortOrder = "S",
                    nextOffset = 0,
                    showListAsPickup = false,
                    stores = responseList
                )
            )
        )

        val expectedStoreFeedResult: StoreFeedResult = repository.getStoreFeed(0, 10, 0f, 0f)
        assertTrue(expectedStoreFeedResult is StoreFeedResult.Success)
        val expectedSuccessResult = expectedStoreFeedResult as StoreFeedResult.Success
        assertEquals(expectedSuccessResult.storeFeed?.stores, responseList)
    }

    @Test
    fun `fetch store feed error`() = runBlocking {
        Mockito.`when`(
            doorDashService.getStoreFeed(
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(
            Mockito.mock(Call::class.java) as Call<StoreFeed>
        )

        val call = doorDashService.getStoreFeed(0f, 0f, 1, 1)

        Mockito.`when`(call.execute()).thenReturn(
            Response.error(
                404,
                ResponseBody.create(
                    MediaType.parse("text/plain"),
                    "No stores for the provided location!!"
                )
            )
        )

        val expectedStoreFeedResult: StoreFeedResult = repository.getStoreFeed(0, 10, 37.3f, 122.4f)
        assertTrue(expectedStoreFeedResult is StoreFeedResult.Error)

        val expectedErrorResult = expectedStoreFeedResult as StoreFeedResult.Error
        assertEquals(
            expectedErrorResult.exception.errorMessage,
            "No stores for the provided location!!"
        )
    }

    @Test
    fun `fetch store detail success`() = runBlocking {
        val store = StoreDetails(
            30,
            "Thailand",
            "Coconout soup",
            null,
            4.5f,
            "25 - 30 Mins",
            address = null

        )
        Mockito.`when`(
            doorDashService.getStoreDetails(ArgumentMatchers.anyInt())
        ).thenReturn(
            Mockito.mock(Call::class.java) as Call<StoreDetails>
        )

        val call = doorDashService.getStoreDetails(1)

        Mockito.`when`(call.execute()).thenReturn(
            Response.success(
                store

            )
        )

        val expectedStoreDetailResult: StoreDetailResult = repository.getStoreDetails(0)
        assertTrue(expectedStoreDetailResult is StoreDetailResult.Success)
        val expectedSuccessResult = expectedStoreDetailResult as StoreDetailResult.Success
        assertEquals(expectedSuccessResult.store, store)
    }

    @Test
    fun `fetch store detail error`() = runBlocking {
        Mockito.`when`(
            doorDashService.getStoreDetails(ArgumentMatchers.anyInt())
        ).thenReturn(
            Mockito.mock(Call::class.java) as Call<StoreDetails>
        )

        val call = doorDashService.getStoreDetails(1)


        Mockito.`when`(call.execute()).thenReturn(
            Response.error(
                404,
                ResponseBody.create(
                    MediaType.parse("text/plain"),
                    "No store Detail Found!!"
                )
            )
        )

        val expectedStoreDetailResult: StoreDetailResult = repository.getStoreDetails(0)
        assertTrue(expectedStoreDetailResult is StoreDetailResult.Error)

        val expectedErrorResult = expectedStoreDetailResult as StoreDetailResult.Error
        assertEquals(expectedErrorResult.exception.errorMessage, "No store Detail Found!!"
        )
    }
}