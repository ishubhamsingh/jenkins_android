package com.ishubhamsingh.jenkins.core.retrofit

import android.content.Context
import com.ishubhamsingh.jenkins.core.exception.NoConnectivityException
import com.ishubhamsingh.jenkins.utils.Utilities
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Shubham Singh on 14-07-2019.
 */

open class ConnectivityInterceptor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!Utilities.isInternetConnected(context)) {
            throw NoConnectivityException()
        }
        return chain.proceed(chain.request())

    }
}