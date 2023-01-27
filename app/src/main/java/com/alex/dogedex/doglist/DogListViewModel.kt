package com.alex.dogedex.doglist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.dogedex.model.Dog
import com.alex.dogedex.api.ApiResponseStatus
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

    var dogList = mutableStateOf<List<Dog>>(listOf())
    private set

    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)

    private val dogRepository = DogRepository()

    init {
        getDogCollection()
    }

    private fun getDogCollection(){
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleResponseStatus(dogRepository.getDogCollection())
        }
    }

//    private fun downloadUserDogs(){
//        viewModelScope.launch {
//            _status.value = ApiResponseStatus.Loading()
//            handleResponseStatus(dogRepository.getUserDogs())
//        }
//    }

//    private fun downloadDogs() {
//        viewModelScope.launch {
//            _status.value = ApiResponseStatus.Loading()
//            handleResponseStatus(dogRepository.downloadDogs())
//        }
//    }

    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            dogList.value = apiResponseStatus.data
        }

        status.value = apiResponseStatus as ApiResponseStatus<Any>
    }
}