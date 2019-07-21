package com.ishubhamsingh.jenkins.viewmodels

import androidx.lifecycle.MutableLiveData
import com.ishubhamsingh.jenkins.core.viewmodel.BaseViewModel
import com.ishubhamsingh.jenkins.interactor.GetJenkinsDataUsecase
import com.ishubhamsingh.jenkins.interactor.UseCase
import com.ishubhamsingh.jenkins.models.Home
import okhttp3.Headers
import javax.inject.Inject

/**
 * Created by Shubham Singh on 15-07-2019.
 */

class JobListViewModel @Inject constructor(val getJenkinsDataUsecase: GetJenkinsDataUsecase) : BaseViewModel() {

    val homeData:MutableLiveData<Home> = MutableLiveData()

    fun fetchJobsList() {
        getJenkinsDataUsecase.execute({it.either(::handleFailure, ::handleResponse)}, UseCase.None())
    }

    private fun handleResponse(response: Home) {
        homeData.value = response
    }

    fun onDestroy() {
        getJenkinsDataUsecase.cancelUseCase()
    }
}