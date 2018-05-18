package com.ishubhamsingh.jenkins.models

data class Home (val mode:String?, val nodeName:String?, val numExecutors:Int?,
                 val description:String?, val jobs:ArrayList<Job>?, val views:ArrayList<View>?)