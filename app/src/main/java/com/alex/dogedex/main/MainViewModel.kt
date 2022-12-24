package com.alex.dogedex.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.doglist.DogRepository
import com.alex.dogedex.model.Dog
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog> get() = _dog

    private val _status = MutableLiveData<ApiResponseStatus<Dog>>()
    val status: LiveData<ApiResponseStatus<Dog>> get() = _status

    private val dogRepository = DogRepository()
    fun getDogByMlId(mlDogId: String){
        viewModelScope.launch {
            handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Dog>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dog.value = apiResponseStatus.data!!
        }

        _status.value = apiResponseStatus
    }
}