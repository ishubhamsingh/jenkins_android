package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.data.Preferences
import com.ishubhamsingh.jenkins.databinding.ActivityDashboardBinding
import dagger.android.AndroidInjection
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import javax.inject.Inject


class DashboardActivity: AppCompatActivity() ,AnkoLogger{

    private lateinit var mBinding: ActivityDashboardBinding

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        mBinding.toolbar.title = getString(R.string.hi,preferences.fetchString(Constants.KEY_USERNAME))


        mBinding.tvInfoVersion.text = preferences.fetchString(Constants.KEY_JENKINS_VERSION)
        mBinding.tvInfoUrl.text = preferences.fetchString(Constants.KEY_URL)


        if(!preferences.fetchBoolean(Constants.KEY_IS_AUTHORISED)) {

            mBinding.fabCreate.isEnabled = false
            mBinding.fabCreate.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.grey2))
            mBinding.fabRestart.isEnabled = false
            mBinding.fabRestart.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext,R.color.grey2))

        }

        setButtons()

        mBinding.toolbar.subtitle = getString(R.string.welcome,preferences.fetchString(Constants.KEY_NAME))

    }

    private fun setButtons(){

        mBinding.fabJobs.setOnClickListener {

            startActivity<JobsListActivity>()

        }

        mBinding.fabViews.setOnClickListener {

        }

        mBinding.fabQueue.setOnClickListener {

        }

        mBinding.fabNodes.setOnClickListener {

        }

        mBinding.fabStats.setOnClickListener {

        }

        mBinding.fabCreate.setOnClickListener {

        }

        mBinding.fabRestart.setOnClickListener {

        }

        mBinding.fabLogout.setOnClickListener {

            preferences.clearPreferences()
            startActivity<SetupActivity>()
            finish()

        }

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

}