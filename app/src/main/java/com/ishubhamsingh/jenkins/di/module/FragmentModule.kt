package com.ishubhamsingh.jenkins.di.module

import com.ishubhamsingh.jenkins.fragments.AuthenticationFragment
import com.ishubhamsingh.jenkins.fragments.InstanceSetupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Shubham Singh on 14-07-2019.
 */

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeInstanceSetupFragment(): InstanceSetupFragment

    @ContributesAndroidInjector
    internal abstract fun contributeAuthenticationFragment(): AuthenticationFragment

}