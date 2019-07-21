package com.ishubhamsingh.jenkins.interactor

import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.core.exception.Failure
import com.ishubhamsingh.jenkins.core.functional.Either
import com.ishubhamsingh.jenkins.data.Preferences
import com.ishubhamsingh.jenkins.models.Home
import okhttp3.Headers
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

/**
 * Created by Shubham Singh on 15-07-2019.
 */

class GetJenkinsDataUsecase @Inject constructor(val preferences: Preferences) : UseCase<Home, UseCase.None>() {

    var url: String = preferences.fetchString(Constants.KEY_URL)

    override suspend fun run(): Either<Failure, Home> {
        val result: Result<Home> = if(preferences.fetchBoolean(Constants.KEY_IS_AUTHORISED)) {jenkinsAuthApi.getJenkinsInstanceInfo("$url/api/json/").awaitResult() } else {jenkinsApi.getJenkinsInstanceInfo("$url/api/json/").awaitResult()}
        return when (result) {
            //Successful HTTP result
            is Result.Ok -> Either.Right(result.value)
            // Any HTTP error
            is Result.Error -> Either.Left(getFailure(result))
            // Exception while request invocation
            is Result.Exception -> Either.Left(getFailureException(result.exception))
        }

    }

}