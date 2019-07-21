package com.ishubhamsingh.jenkins.di.module

import android.content.Context
import com.ishubhamsingh.jenkins.BuildConfig
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.JenkinsApplication
import com.ishubhamsingh.jenkins.core.retrofit.AuthRequestInterceptor
import com.ishubhamsingh.jenkins.core.retrofit.ConnectivityInterceptor
import com.ishubhamsingh.jenkins.core.retrofit.NetworkHandler
import com.ishubhamsingh.jenkins.data.Preferences
import com.ishubhamsingh.jenkins.interactor.api.JenkinsApi
import com.ishubhamsingh.jenkins.interactor.api.JenkinsAuthApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Shubham Singh on 14-07-2019.
 */

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: JenkinsApplication): Context = application

    @Provides
    @Singleton
    fun providesPreferences(application: JenkinsApplication): Preferences {
        return Preferences(application)
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if (BuildConfig.BUILD_TYPE.equals("debug", ignoreCase = true))
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        return httpLoggingInterceptor

    }

    @Provides
    @Singleton
    fun providesJenkinsAuthApi(authRequestInterceptor: AuthRequestInterceptor, connectivityInterceptor: ConnectivityInterceptor,
                                httpLoggingInterceptor: HttpLoggingInterceptor, preferences: Preferences): JenkinsAuthApi {


        val builder = OkHttpClient.Builder()
        builder.interceptors().add(authRequestInterceptor)
        builder.interceptors().add(connectivityInterceptor)
        builder.interceptors().add(httpLoggingInterceptor)

        builder.readTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(JenkinsAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun providesJenkinsApi(connectivityInterceptor: ConnectivityInterceptor,
                                httpLoggingInterceptor: HttpLoggingInterceptor, preferences: Preferences): JenkinsApi {


        val builder = OkHttpClient.Builder()
        builder.interceptors().add(connectivityInterceptor)
        builder.interceptors().add(httpLoggingInterceptor)

        builder.readTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(JenkinsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRequestInterceptor(preferences: Preferences): AuthRequestInterceptor {
        return AuthRequestInterceptor(preferences)
    }
    @Provides
    @Singleton
    fun providesConnectivityInterceptor(context: Context): ConnectivityInterceptor {
        return ConnectivityInterceptor(context)
    }

    @Provides
    @Singleton
    fun providesNetworkHandler(application: JenkinsApplication): NetworkHandler {
        return NetworkHandler(application)

    }
}