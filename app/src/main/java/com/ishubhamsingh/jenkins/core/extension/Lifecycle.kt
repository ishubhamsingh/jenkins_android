package com.ishubhamsingh.jenkins.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ishubhamsingh.jenkins.core.exception.Failure

/**
 * Created by Shubham Singh on 14-07-2019.
 */

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L?, body: (T?) -> Unit) =
    liveData?.observe(this, Observer(body))

fun <L : LiveData<Failure>> LifecycleOwner.failure(liveData: L, body: (Failure?) -> Unit) =
    liveData.observe(this, Observer(body))