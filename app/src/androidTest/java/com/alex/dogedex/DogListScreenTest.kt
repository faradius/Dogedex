package com.alex.dogedex

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.doglist.DogListScreen
import com.alex.dogedex.doglist.DogListViewModel
import com.alex.dogedex.doglist.DogTasks
import com.alex.dogedex.model.Dog
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
class DogListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalMaterialApi
    @Test
    fun progressBar_shows_when_loading_state(){
        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Loading()
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent { 
            DogListScreen(
                onNavigationIconClick = { },
                onDogClicked = { },
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(testTag = "loading-wheel").assertIsDisplayed()
    }


    @ExperimentalMaterialApi
    @Test
    fun error_dialog_shows_if_error_getting_dogs(){
        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(messageId = R.string.there_was_an_error)
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { },
                onDogClicked = { },
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(testTag = "error-dialog").assertIsDisplayed()
    }

    @ExperimentalMaterialApi
    @Test
    fun dogList_shows_if_success_getting_dogs(){
        val dog1Name = "Chihuahua"
        val dog2Name = "Guilem"
        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Success(
                    listOf(
                        Dog(1, 1, dog1Name, "", "", "", "", "", "", "", ""
                            , inColletion = true),
                        Dog(19, 23, dog2Name, "", "", "", "", "", "", "", ""
                            , inColletion = false)
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { },
                onDogClicked = { },
                viewModel = viewModel
            )
        }

        //para poder separar de los nodos hijos de los nodos padres nececitamos el useUnmergedTree
//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("Manzana")
        //solo se necesita cuando se usa semantics
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "dog-$dog1Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("23").assertIsDisplayed()
    }
}