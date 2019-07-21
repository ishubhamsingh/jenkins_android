package com.ishubhamsingh.jenkins.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Base64

/**
 * Created by Shubham Singh on 14-07-2019.
 */

object Utilities {

    fun isInternetConnected(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    fun getAuthorizationBearer(username:String, password:String): String {
        val authorizationText = "$username:$password"
        return "Basic " + Base64.encodeToString(authorizationText.toByteArray(), Base64.NO_WRAP)
    }
}