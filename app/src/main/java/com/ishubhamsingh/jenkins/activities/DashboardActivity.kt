package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper



class DashboardActivity: AppCompatActivity() ,AnkoLogger{

    private lateinit var prefJenkins: SharedPreferences
    private lateinit var prefAccount: SharedPreferences
    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Rubik-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        mCompositeDisposable = CompositeDisposable()
        prefAccount=getSharedPreferences(Constants.PREFS_ACCOUNT,Context.MODE_PRIVATE)
        prefJenkins=getSharedPreferences(Constants.PREFS_ACCOUNT,Context.MODE_PRIVATE)

        if(prefAccount.getBoolean(Constants.KEY_IS_AUTHORISED, false)) {


        } else {


        }

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }

}