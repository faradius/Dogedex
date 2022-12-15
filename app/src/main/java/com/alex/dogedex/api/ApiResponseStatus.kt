package com.alex.dogedex.api

import com.alex.dogedex.Dog

sealed class ApiResponseStatus() {
    class Success(val dogList: List<Dog>):ApiResponseStatus()
    class Loading():ApiResponseStatus()
    class Error(val messageId: Int):ApiResponseStatus()
}