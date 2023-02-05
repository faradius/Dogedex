package com.alex.dogedex.auth

import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.api.ApiService
import com.alex.dogedex.api.dto.LoginDTO
import com.alex.dogedex.api.dto.SignUpDTO
import com.alex.dogedex.api.dto.UserDTOMapper
import com.alex.dogedex.api.makeNetworkCall
import com.alex.dogedex.model.User
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface AuthTasks{
    suspend fun login(email:String, password: String): ApiResponseStatus<User>
    suspend fun signUp(email:String, password: String, passwordConfirmation: String): ApiResponseStatus<User>
}

class AuthRepository @Inject constructor(
    private val apiService: ApiService
):AuthTasks{

    override suspend fun login(
        email:String,
        password: String
    ): ApiResponseStatus<User> = makeNetworkCall{
        val loginDTO = LoginDTO(email, password)
        val loginResponse = apiService.login(loginDTO)

        if(!loginResponse.isSuccess){
            //Si no es exitoso hacemos que tire una excepción
            throw Exception(loginResponse.message)
        }

        val userDTO = loginResponse.data.user
        val userDTOMapper = UserDTOMapper()
        //Esto es el resultato que contendrá call
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }

    override suspend fun signUp(
        email:String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> = makeNetworkCall{
        val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
        val signUpResponse = apiService.signUp(signUpDTO)

        if(!signUpResponse.isSuccess){
            //Si no es exitoso hacemos que tire una excepción
            throw Exception(signUpResponse.message)
        }

        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
        //Esto es el resultato que contendrá call
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }
}