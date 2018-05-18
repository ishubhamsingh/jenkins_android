package com.ishubhamsingh.jenkins.interfaces

import com.ishubhamsingh.jenkins.models.Home
import io.reactivex.Observable
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Header

interface RequestInterface {

    @GET("/api/json/")
    fun getResult(): Observable<Result<Home>>

    @GET("/api/json/")
    fun getResultAuth(@Header("Authorization") authKey:String) : Observable<Result<Home>>
}