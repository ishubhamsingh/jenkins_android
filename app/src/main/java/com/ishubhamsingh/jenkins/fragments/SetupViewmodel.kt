package com.ishubhamsingh.jenkins.fragments

import androidx.lifecycle.MutableLiveData
import com.ishubhamsingh.jenkins.core.viewmodel.BaseViewModel
import com.ishubhamsingh.jenkins.interactor.GetJenkinsInfoUsecase
import com.ishubhamsingh.jenkins.interactor.UseCase
import okhttp3.Headers
import javax.inject.Inject

/**
 * Created by Shubham Singh on 15-07-2019.
 */

class SetupViewmodel @Inject constructor(val getJenkinsInfoUsecase: GetJenkinsInfoUsecase): BaseViewModel() {

    var jenkinsInfoData: MutableLiveData<Pair<Int,Headers>> = MutableLiveData()

    fun getHomeResult(url:String = "") {
        if(url.isNotEmpty()) getJenkinsInfoUsecase.url = url
        getJenkinsInfoUsecase.execute({it.either(::handleFailure, ::handleResponse)}, UseCase.None())
    }

    private fun handleResponse(response: Pair<Int, Headers>) {
        jenkinsInfoData.value = response
    }

    fun onDestroy() {
        getJenkinsInfoUsecase.cancelUseCase()
    }
}