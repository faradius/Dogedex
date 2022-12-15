package com.alex.dogedex.doglist

import com.alex.dogedex.Dog
import com.alex.dogedex.R
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.api.DogsApi
import com.alex.dogedex.api.DogsApi.retrofitService
import com.alex.dogedex.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class DogRepository {
    suspend fun downloadDogs(): ApiResponseStatus{
        return withContext(Dispatchers.IO){
            //El ultimo valor de la lambda es lo que va a regresar
            try {
                val dogListApiResponse = retrofitService.getAllDogs()
                val dogDtoList = dogListApiResponse.data.dogs
                val dogDTOMapper = DogDTOMapper()
                ApiResponseStatus.Success(dogDTOMapper.fromDogDTOListToDogDomainList(dogDtoList))
            } catch (e: UnknownHostException){
                ApiResponseStatus.Error(R.string.unknown_host_exception_error)
            } catch (e: Exception){
                ApiResponseStatus.Error(R.string.unknown_error)
            }
        }
    }
}