package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.os.Bundle
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.data.Preferences
import dagger.android.AndroidInjection
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import javax.inject.Inject


class SplashActivity : androidx.fragment.app.FragmentActivity(), AnkoLogger {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_splash)
        val splashThread=SplashThread()
        splashThread.start()

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    inner class SplashThread : Thread() {

        override fun run() {
            sleep(2000)
            if (!preferences.fetchBoolean(Constants.ACCOUNT_SETUP_DONE)) {

                startActivity<SetupActivity>()

            } else if (preferences.fetchBoolean(Constants.ACCOUNT_SETUP_DONE)) {

                startActivity<DashboardActivity>()

            } else {

                startActivity<SetupActivity>()
                info("ErrorPrefs")
            }

            finish()
        }

    }


}