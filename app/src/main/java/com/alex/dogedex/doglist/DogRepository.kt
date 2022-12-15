package com.alex.dogedex.doglist

import com.alex.dogedex.Dog
import com.alex.dogedex.R
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.api.DogsApi
import com.alex.dogedex.api.DogsApi.retrofitService
import com.alex.dogedex.api.dto.DogDTOMapper
import com.alex.dogedex.api.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class DogRepository {
    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall{
            val dogListApiResponse = retrofitService.getAllDogs()
            val dogDtoList = dogListApiResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            //Esto es el resultato que contendrá call
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDtoList)
    }
}
