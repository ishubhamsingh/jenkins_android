package com.ishubhamsingh.jenkins.di.module

import com.ishubhamsingh.jenkins.activities.DashboardActivity
import com.ishubhamsingh.jenkins.activities.JobsListActivity
import com.ishubhamsingh.jenkins.activities.SetupActivity
import com.ishubhamsingh.jenkins.activities.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Shubham Singh on 14-07-2019.
 */

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeSetupActivity(): SetupActivity

    @ContributesAndroidInjector
    abstract fun contributeDahboardActivity(): DashboardActivity

    @ContributesAndroidInjector
    abstract fun contributeJoblistActivity(): JobsListActivity
}