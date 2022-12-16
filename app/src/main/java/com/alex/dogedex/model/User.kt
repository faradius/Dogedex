package com.alex.dogedex.model

import com.squareup.moshi.Json

data class User(
    val id:Long,
    val email: String,
//    val password: String,
//    val passworConfirmation: String,
    val authenticationToken: String
)