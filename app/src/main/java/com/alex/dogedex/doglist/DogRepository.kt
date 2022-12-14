package com.alex.dogedex.doglist

import com.alex.dogedex.Dog
import com.alex.dogedex.api.DogsApi
import com.alex.dogedex.api.DogsApi.retrofitService
import com.alex.dogedex.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {
    suspend fun downloadDogs(): List<Dog>{
        return withContext(Dispatchers.IO){
            //El ultimo valor de la lambda es lo que va a regresar
            val dogListApiResponse = retrofitService.getAllDogs()
            val dogDtoList = dogListApiResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDtoList)


        }
    }
}