package com.ishubhamsingh.jenkins

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.ishubhamsingh.jenkins.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import javax.inject.Inject


/**
 * Created by Shubham Singh on 14-07-2019.
 */

class JenkinsApplication : MultiDexApplication(), HasAndroidInjector {

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Rubik-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        DaggerApplicationComponent
            .builder().application(this)
            .build().inject(this)

    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}