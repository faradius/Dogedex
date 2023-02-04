package com.alex.dogedex.doglist

import com.alex.dogedex.R
import com.alex.dogedex.model.Dog
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.api.ApiService
import com.alex.dogedex.api.dto.AddDogToUserDTO
import com.alex.dogedex.api.dto.DogDTOMapper
import com.alex.dogedex.api.makeNetworkCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DogTasks{
    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>>
    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any>
    suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog>
}
class DogRepository @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
) : DogTasks{
    override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
        return withContext(dispatcher) {
            val allDogsListDeferred = async { downloadDogs() }
            val userDogsListDeferred = async { getUserDogs() }

            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse = userDogsListDeferred.await()

            if (allDogsListResponse is ApiResponseStatus.Error) {
                allDogsListResponse
            } else if (userDogsListResponse is ApiResponseStatus.Error) {
                userDogsListResponse
            } else if (allDogsListResponse is ApiResponseStatus.Success && userDogsListResponse is ApiResponseStatus.Success) {
                ApiResponseStatus.Success(
                    getCollectionList(
                        allDogsListResponse.data,
                        userDogsListResponse.data
                    )
                )
            } else {
                ApiResponseStatus.Error(R.string.unknown_error)
            }
        }
    }


    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>): List<Dog> {
        //Se toma toda la coleccion de perros
        return allDogList.map {
            //Para cada uno de esos perro se checa si esta en la colección
            if (userDogList.contains(it)) {
                //Si esta en la colleción se devuelve ese perro
                it
                //Si no se devuelve un perro con datos vacios
            } else {
                Dog(0, it.index, "", "", "", "", "", "", "", "", ""
                , inColletion = false)
            }
        }.sorted()
    }

    private suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = apiService.getAllDogs()
        val dogDtoList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        //Esto es el resultato que contendrá call
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDtoList)
    }

    private suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = apiService.getUserDogs()
        val dogDtoList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        //Esto es el resultato que contendrá call
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDtoList)
    }

    override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUserDTO(dogId)
        val defaultResponse = apiService.addDogToUser(addDogToUserDTO)

        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }

    override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> = makeNetworkCall {
        val response = apiService.getDogByMlId(mlDogId)

        if (!response.isSuccess){
            throw Exception(response.message)
        }

        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOToDogDomain(response.data.dog)

    }
}
