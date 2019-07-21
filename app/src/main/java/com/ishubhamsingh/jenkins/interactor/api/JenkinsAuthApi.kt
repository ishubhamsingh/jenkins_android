package com.ishubhamsingh.jenkins.interactor.api

import com.ishubhamsingh.jenkins.models.Home
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Shubham Singh on 15-07-2019.
 */

interface JenkinsAuthApi {

    @GET
    fun getJenkinsInstanceInfo(@Url jenkinsBaseUrl: String): Call<Home>

}