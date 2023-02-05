package com.alex.dogedex.viewmodel

import com.alex.dogedex.R
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.auth.AuthTasks
import com.alex.dogedex.auth.AuthViewModel
import com.alex.dogedex.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var dogedexCoroutineRule = DogedexCoroutineRule()

    @Test
    fun test_login_validations_correct(){
        class FakeAuthRepository: AuthTasks{
            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(
                    User(1,"alan@gmail.com","")
                )
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(
                    User(1,"","")
                )
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        viewModel.login("","test1234")
        assertEquals(R.string.email_is_not_valid,viewModel.emailError.value)
        viewModel.login("alan@gmail.com","")
        assertEquals(R.string.password_must_not_be_empty,viewModel.passwordError.value)
    }

    @Test
    fun test_login_states_correct(){
        val fakeUser =  User(1,"alan@gmail.com","")

        class FakeAuthRepository: AuthTasks{
            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(fakeUser)
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(
                    User(1,"","")
                )
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        viewModel.login("alan@gmail.com","test1234")
        assertEquals(fakeUser.email, viewModel.user.value?.email)

    }
}