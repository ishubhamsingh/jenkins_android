package com.ishubhamsingh.jenkins.core.exception

import com.ishubhamsingh.jenkins.models.ErrorBody
import retrofit2.HttpException

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    object NetworkConnection : Failure()
    class HttpError(val throwable: HttpException) : Failure()
    object NetworkException : Failure()
    class ServerError(val code : Int, val errorBody : ErrorBody) : Failure()
    object IOException : Failure()
    object IllegalStateException : Failure()
    class AccountMisMatchException( userIdentifier : String) : Failure()
    class UnKnownException(val throwable: Throwable) : Failure()
    object UnKnownDataLoadException : Failure()


    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()

}