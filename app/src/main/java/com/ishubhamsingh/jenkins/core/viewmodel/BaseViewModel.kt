package com.ishubhamsingh.jenkins.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishubhamsingh.jenkins.core.exception.Failure

/**
 * Created by Shubham Singh on 14-07-2019.
 */

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()


    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }


}