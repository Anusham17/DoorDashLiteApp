package com.example.doordash.viewmodel

import androidx.lifecycle.*
import com.example.doordash.repository.DoorDashRepository
import com.example.doordash.repository.remote.model.NetworkException
import com.example.doordash.repository.remote.model.StoreDetailResult
import com.example.doordash.repository.remote.model.StoreDetails
import kotlinx.coroutines.launch

class StoreDetailViewModel(val repository: DoorDashRepository) :  ViewModel()  {

    private val _storeDetailLiveData = MutableLiveData<StoreDetails>()
    val storeDetailLiveData: LiveData<StoreDetails> = _storeDetailLiveData

    private val _errorLiveData = MutableLiveData<NetworkException>()
    val errorLiveData: LiveData<NetworkException> = _errorLiveData

    /**
     *  Fetches store details for the supplied store id and posts the result to success/error livedata
     */
    fun getStoreDetails(id: Int) {
        viewModelScope.launch {
            repository.getStoreDetails(id).let { result ->
                when (result) {
                    is StoreDetailResult.Success -> {
                        _storeDetailLiveData.postValue(
                            result.store
                        )
                    }
                    is StoreDetailResult.Error -> {
                        _errorLiveData.postValue(result.exception)
                    }
                }
            }
        }
    }

    class Factory(val repository: DoorDashRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            StoreDetailViewModel(repository) as T
    }
}