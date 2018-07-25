package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
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
        prefJenkins=getSharedPreferences(Constants.PREFS_JENKINS_DETAILS,Context.MODE_PRIVATE)

        toolbar.title = getString(R.string.hi,prefAccount.getString(Constants.KEY_USERNAME,"Anonymous"))
        toolbar.subtitle = getString(R.string.welcome,prefJenkins.getString(Constants.KEY_NAME,"jenkins"))

        tv_info_version.text = prefJenkins.getString(Constants.KEY_JENKINS_VERSION,"Unknown")
        tv_info_url.text = prefJenkins.getString(Constants.KEY_URL,"Unknown")


        if(prefAccount.getBoolean(Constants.KEY_IS_AUTHORISED, false)) {


        } else {

            fab_create.isEnabled = false
            fab_create.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.grey2))
            fab_restart.isEnabled = false
            fab_restart.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.grey2))

        }

        setButtons()

    }

    private fun setButtons(){

        fab_jobs.setOnClickListener {

            startActivity<JobsListActivity>()

        }

        fab_views.setOnClickListener {

        }

        fab_queue.setOnClickListener {

        }

        fab_nodes.setOnClickListener {

        }

        fab_stats.setOnClickListener {

        }

        fab_create.setOnClickListener {

        }

        fab_restart.setOnClickListener {

        }

        fab_logout.setOnClickListener {

            prefAccount.edit().clear().apply()
            prefJenkins.edit().clear().apply()
            startActivity<SetupActivity>()
            finish()

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