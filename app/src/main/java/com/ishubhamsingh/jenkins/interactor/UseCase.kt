package com.ishubhamsingh.jenkins.interactor

import com.google.gson.Gson
import com.ishubhamsingh.jenkins.core.exception.Failure
import com.ishubhamsingh.jenkins.core.functional.Either
import com.ishubhamsingh.jenkins.core.retrofit.NetworkHandler
import com.ishubhamsingh.jenkins.interactor.api.JenkinsApi
import com.ishubhamsingh.jenkins.interactor.api.JenkinsAuthApi
import com.ishubhamsingh.jenkins.models.ErrorBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import ru.gildor.coroutines.retrofit.Result
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Shubham Singh on 14-07-2019.
 */

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class UseCase<out Type, in Params>() where Type : Any {
    protected var userIdentifier = ""
    private var job: Job? = null

    @Inject
    lateinit var networkHandler: NetworkHandler

    @Inject
    lateinit var jenkinsApi: JenkinsApi

    @Inject
    lateinit var jenkinsAuthApi: JenkinsAuthApi


    abstract suspend fun run(): Either<Failure, Type>


    fun execute(onResult: (Either<Failure, Type>) -> Unit, params: Params) {


        job = GlobalScope.launch(Dispatchers.Main) {

            when (networkHandler.isConnected) {

                true -> {

                    onResult.invoke(run())

                }
                false, null -> {
                    onResult.invoke(Either.Left(Failure.NetworkConnection))

                }
            }

        }

    }

    protected fun getFailure(response: Result.Error): Failure {
        return when (response.exception.code()) {
            401 -> Failure.AccountMisMatchException(userIdentifier)
            else -> {

                var errorBody = ErrorBody()
                errorBody.code = response.response.code().toString()
                errorBody.message = response.response.message()

                val error = response.exception.response().errorBody()?.charStream()

                if (error != null) {
                    try {
                        errorBody = Gson().fromJson(error, ErrorBody::class.java)
                    } catch (e: Exception) {

                    }
                }
                Failure.ServerError(response.exception.code(), errorBody)
            }
        }
    }

    protected fun getFailureError(errorCode: Int, response: ResponseBody?): Failure {
        var errorBody = ErrorBody()
        try {
            response?.let {
                errorBody = Gson().fromJson(it.charStream(), ErrorBody::class.java)
            }
        } catch (e: Exception) {

        }
        return Failure.ServerError(errorCode, errorBody)
    }


    protected fun getFailureException(throwable: Throwable): Failure {
        throwable.printStackTrace()
        return when (throwable) {
            is IOException -> Failure.NetworkException
            is HttpException -> Failure.HttpError(throwable)
            else -> Failure.UnKnownException(throwable)
        }
    }

    class None

    /**
     * Cancels this job with an optional cancellation [cause]
     */
    fun cancelUseCase() {
        job?.run {
            if (isActive) {
                cancel()
            }
        }

    }

    fun isUseCaseCompleted(): Boolean {
        return job?.isCompleted ?: true
    }


}