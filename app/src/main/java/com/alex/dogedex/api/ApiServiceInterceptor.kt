package com.alex.dogedex.api

import okhttp3.Interceptor
import okhttp3.Response

//La libreria okhttp3.Intercpetor nos permite mejorar las peticiones http
object ApiServiceInterceptor: Interceptor {
    const val NEEDS_AUTH_HEADER_KEY = "needs_authentication"

    private var sessionToken: String? = null

    fun setSessionToken(sessionToken: String){
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        //request.headers[NEEDS_AUTH_HEADER_KEY]
        if (request.header(NEEDS_AUTH_HEADER_KEY) != null){
            if (sessionToken == null){
                throw RuntimeException("Need to be authenticated")
            }else{
                requestBuilder.addHeader("AUTH-TOKEN", sessionToken!!)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}