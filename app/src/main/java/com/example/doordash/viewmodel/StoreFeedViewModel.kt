package com.example.doordash.viewmodel

import androidx.lifecycle.*
import com.example.doordash.repository.DoorDashRepository
import com.example.doordash.repository.remote.model.NetworkException
import com.example.doordash.repository.remote.model.Store
import com.example.doordash.repository.remote.model.StoreFeedResult
import com.example.doordash.util.Event
import kotlinx.coroutines.launch

class StoreFeedViewModel(val repository: DoorDashRepository) : ViewModel() {

    private val _storesLiveData = MutableLiveData<MutableList<Store>>()
    val storesLiveData: LiveData<MutableList<Store>> = _storesLiveData

    private val _storeItemClickLiveData = MutableLiveData<Event<Int>>()
    val storeItemClickLiveData: LiveData<Event<Int>> = _storeItemClickLiveData

    private val _errorLiveData = MutableLiveData<NetworkException>()
    val errorLiveData: LiveData<NetworkException> = _errorLiveData

    /**
     *  Fetches store feed and posts the result to success/error livedata
     */
    fun getStores(offset: Int) {
        viewModelScope.launch {
            repository.getStoreFeed(offset, 10, 37.422740f, -122.139956f).let { result ->
                when (result) {
                    is StoreFeedResult.Success -> {
                        _storesLiveData.postValue(
                            _storesLiveData.value?.apply { result.storeFeed?.stores?.let { addAll(it) } }
                                ?: run { result.storeFeed?.stores?.toMutableList() }
                        )
                    }
                    is StoreFeedResult.Error -> {
                        _errorLiveData.postValue(result.exception)
                    }
                }
            }
        }
    }

    /**
     *  Posts the supplied store id to store item click handler
     */
    fun onStoreItemClick(storeId: Int) {
        _storeItemClickLiveData.postValue(Event(storeId))
    }

    class Factory(val repository: DoorDashRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            StoreFeedViewModel(repository) as T
    }
}