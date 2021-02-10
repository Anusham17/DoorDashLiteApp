package com.example.doordash.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.doordash.TestCoroutineRule
import com.example.doordash.repository.DoorDashRepository
import com.example.doordash.repository.remote.model.Store
import com.example.doordash.repository.remote.model.StoreFeed
import com.example.doordash.repository.remote.model.StoreFeedResult
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class StoreFeedViewModelTest {

    @get:Rule
    internal val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private lateinit var viewModel: StoreFeedViewModel
    private var repo = mock(DoorDashRepository::class.java)
    private lateinit var mockObserver: Observer<MutableList<Store>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = StoreFeedViewModel(repo)
        mockObserver = mock(Observer::class.java) as Observer<MutableList<Store>>
    }

    @Test
    fun `fetch stores`() = coroutineTestRule.runBlockingTest {
        val store = Store(
            30,
            "Thai Restaurant",
            "coconut soup, thai noodles",
            null,
            null
        )
        val stores = mutableListOf(store)
        `when`(repo.getStoreFeed(anyInt(), anyInt(), anyFloat(), anyFloat()))
            .thenReturn(StoreFeedResult.Success(StoreFeed(40, true, "s", 5, false, stores)))
        viewModel.storesLiveData.observeForever(mockObserver)
        viewModel.getStores(0)


        assertEquals(stores, viewModel.storesLiveData.value)
    }
}