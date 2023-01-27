package com.alex.dogedex.dogdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.doglist.DogRepository
import kotlinx.coroutines.launch

class DogDetailViewModel: ViewModel() {

    //el mutableLiveData ya no se usa en compose
    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
    private set

    private val dogRepository = DogRepository()

    fun addDogToUser(dogId: Long){
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>){
        status.value = apiResponseStatus
    }
}