package com.ishubhamsingh.jenkins.core.extension

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Shubham Singh on 14-07-2019.
 */

val Context.networkInfo get() =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo