package com.alex.dogedex.viewmodel

import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.doglist.DogListViewModel
import com.alex.dogedex.doglist.DogTasks
import com.alex.dogedex.model.Dog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


class DogListViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var dogedexCoroutineRule = DogedexCoroutineRule()

    @Test
    fun download_dogList_status_is_correct(){
        class FakeDogRepository: DogTasks{
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Success(
                    listOf(
                        Dog(1, 1, "", "", "", "", "", "", "", "", ""
                            , inColletion = false),
                        Dog(19, 2, "", "", "", "", "", "", "", "", ""
                            , inColletion = false)
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Success( Dog(1, 1, "", "", "", "", "", "", "", "", ""
                    , inColletion = false))
            }

        }
        //Primero se crea un objeto de la clase a testear
        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(2, viewModel.dogList.value.size)
        //assertEquals(19, viewModel.dogList.value[1].id)
        assert(viewModel.status.value is ApiResponseStatus.Success)
    }

    @Test
    fun download_dogList_status_is_error(){
        class FakeDogRepository: DogTasks{
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(messageId = 12)
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Success( Dog(1, 1, "", "", "", "", "", "", "", "", ""
                    , inColletion = false))
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(0, viewModel.dogList.value.size)
        assert(viewModel.status.value is ApiResponseStatus.Error)
    }

    @Test
    fun reset_status_correct(){
        class FakeDogRepository: DogTasks{
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(messageId = 12)
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Success( Dog(1, 1, "", "", "", "", "", "", "", "", ""
                    , inColletion = false))
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assert(viewModel.status.value is ApiResponseStatus.Error)
        viewModel.resetApiResponseStatus()
        assert(viewModel.status.value == null)
    }
}