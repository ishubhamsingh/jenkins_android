package com.ishubhamsingh.jenkins.core.retrofit

import android.content.Context
import com.ishubhamsingh.jenkins.core.extension.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Shubham Singh on 14-07-2019.
 */

/**
 * Injectable class which returns information about the network connection state.
 */
@Singleton
open class NetworkHandler
@Inject constructor(private val context: Context) {
    open val isConnected get() = context.networkInfo?.isConnectedOrConnecting
}