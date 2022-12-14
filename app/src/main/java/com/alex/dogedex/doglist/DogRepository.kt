package com.alex.dogedex.doglist

import com.alex.dogedex.Dog
import com.alex.dogedex.api.DogsApi
import com.alex.dogedex.api.DogsApi.retrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {
    suspend fun downloadDogs(): List<Dog>{
        return withContext(Dispatchers.IO){
            val dogListApiResponse = retrofitService.getAllDogs()
            //El ultimo valor de la lambda es lo que va a regresar
            dogListApiResponse.data.dogs
        }
    }
}