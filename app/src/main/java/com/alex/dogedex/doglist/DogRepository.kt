package com.alex.dogedex.doglist

import com.alex.dogedex.model.Dog
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.api.DogsApi.retrofitService
import com.alex.dogedex.api.dto.DogDTOMapper
import com.alex.dogedex.api.makeNetworkCall

class DogRepository {
    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall{
            val dogListApiResponse = retrofitService.getAllDogs()
            val dogDtoList = dogListApiResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            //Esto es el resultato que contendrá call
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDtoList)
    }
}
