package com.alex.dogedex.auth

import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.api.DogsApi
import com.alex.dogedex.api.dto.SignUpDTO
import com.alex.dogedex.api.dto.UserDTOMapper
import com.alex.dogedex.api.makeNetworkCall
import com.alex.dogedex.model.User

class AuthRepository {
    suspend fun signUp(
        email:String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> = makeNetworkCall{
        val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
        val signUpResponse = DogsApi.retrofitService.signUp(signUpDTO)

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