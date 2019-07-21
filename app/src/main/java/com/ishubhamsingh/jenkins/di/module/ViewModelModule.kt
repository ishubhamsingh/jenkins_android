package com.ishubhamsingh.jenkins.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ishubhamsingh.jenkins.core.viewmodel.ViewModelFactory
import com.ishubhamsingh.jenkins.core.viewmodel.ViewModelKey
import com.ishubhamsingh.jenkins.fragments.SetupViewmodel
import com.ishubhamsingh.jenkins.viewmodels.JobListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**
 * Created by Shubham Singh on 14-07-2019.
 */

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewmodel::class)
    abstract fun bindsSetupViewModel(setupViewmodel: SetupViewmodel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobListViewModel::class)
    abstract fun bindsJobsListViewModel(jobListViewModel: JobListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

