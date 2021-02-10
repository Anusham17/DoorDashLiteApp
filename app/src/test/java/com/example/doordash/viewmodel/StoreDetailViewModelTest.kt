package com.example.doordash.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.doordash.TestCoroutineRule
import com.example.doordash.repository.DoorDashRepository
import com.example.doordash.repository.remote.model.StoreDetailResult
import com.example.doordash.repository.remote.model.StoreDetails
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class StoreDetailViewModelTest {

    @get:Rule
    internal val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private lateinit var viewModel: StoreDetailViewModel
    private var repo = mock(DoorDashRepository::class.java)
    private lateinit var mockObserver: Observer<StoreDetails>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = StoreDetailViewModel(repo)
        mockObserver = mock(Observer::class.java) as Observer<StoreDetails>
    }

    @Test
    fun `fetch Store Detail`() = coroutineTestRule.runBlockingTest {
        val storeDetails = StoreDetails(
            0,
            "Curry",
            "Some Food",
            null,
            4.5f,
            "closed",
            null
        )
        `when`(repo.getStoreDetails(ArgumentMatchers.anyInt()))
            .thenReturn(
                StoreDetailResult.Success(storeDetails)
            )
        viewModel.storeDetailLiveData.observeForever(mockObserver)
        viewModel.getStoreDetails(0)
        Assert.assertEquals(storeDetails, viewModel.storeDetailLiveData.value)
    }
}