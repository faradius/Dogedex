package com.alex.dogedex.api

import com.alex.dogedex.api.dto.LoginDTO
import com.alex.dogedex.utils.BASE_URL
import com.alex.dogedex.utils.GET_ALL_DOGS_URL
import com.alex.dogedex.utils.SIGN_UP_URL
import com.alex.dogedex.api.dto.SignUpDTO
import com.alex.dogedex.api.responses.DogListApiResponse
import com.alex.dogedex.api.responses.AuthApiResponse
import com.alex.dogedex.utils.SIGN_IN_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService{
    @GET(GET_ALL_DOGS_URL)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @POST(SIGN_IN_URL)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse
}

object DogsApi{
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}