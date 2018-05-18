package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper



class SplashActivity : FragmentActivity(), AnkoLogger {

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Rubik-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        pref=getSharedPreferences(Constants.PREFS_ACCOUNT, Context.MODE_PRIVATE)
        val splashThread=SplashThread()
        splashThread.start()

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    inner class SplashThread : Thread() {

        override fun run() {
            sleep(2000)
            if (!pref.getBoolean(Constants.ACCOUNT_SETUP_DONE, false)) {

                startActivity<SetupActivity>()

            } else if (pref.getBoolean(Constants.ACCOUNT_SETUP_DONE, false)) {

                startActivity<DashboardActivity>()

            } else {

                startActivity<SetupActivity>()
                info("ErrorPrefs")
            }

            finish()
        }

    }


}