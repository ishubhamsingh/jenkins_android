package com.ishubhamsingh.jenkins.di.component

import com.ishubhamsingh.jenkins.JenkinsApplication
import com.ishubhamsingh.jenkins.di.module.ActivityModule
import com.ishubhamsingh.jenkins.di.module.ApplicationModule
import com.ishubhamsingh.jenkins.di.module.FragmentModule
import com.ishubhamsingh.jenkins.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Shubham Singh on 14-07-2019.
 */

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityModule::class,
    FragmentModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent
        @BindsInstance
        fun application(application: JenkinsApplication): Builder
    }

    fun inject(application: JenkinsApplication)


}