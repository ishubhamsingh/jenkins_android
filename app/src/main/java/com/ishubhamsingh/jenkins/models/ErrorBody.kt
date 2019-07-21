package com.ishubhamsingh.jenkins.models

/**
 * Created by Shubham Singh on 14-07-2019.
 */

data class ErrorBody (
    var code: String? = null,
    var error: String? = null,
    var message: String? = null,
    var correlationId: String? = null
)