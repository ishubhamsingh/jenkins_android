package com.ishubhamsingh.jenkins.core.retrofit

import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.data.Preferences
import okhttp3.Interceptor
import okhttp3.ResponseBody
import okhttp3.Response

/**
 * Created by Shubham Singh on 14-07-2019.
 */

open class AuthRequestInterceptor(val preferences: Preferences) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request()
            .newBuilder()
            .addHeader(Constants.AUTH_HEADER, preferences.fetchString(Constants.KEY_AUTH_KEY))
            .addHeader("Accept", "*/*")
            .addHeader("Content-Type", "application/json")

        val newRequest = requestBuilder.build()

        val response = chain.proceed(newRequest)

        val responseBodyString = response.body?.string() ?:""

        return   response.newBuilder()
            .body(ResponseBody.create(response.body?.contentType(), responseBodyString))
            .build()
    }


}