package com.ishubhamsingh.jenkins.core.exception

import com.ishubhamsingh.jenkins.models.ErrorBody

/**
 * Created by Shubham Singh on 14-07-2019.
 */

class NoConnectivityException : RuntimeException() {
    companion object {

        val localNetworkErrorBody: ErrorBody
            get() {
                val errorBody = ErrorBody()
                errorBody.code = "500"
                errorBody.message = "No internet connectivity error"
                errorBody.correlationId = ""
                return errorBody
            }
    }
}