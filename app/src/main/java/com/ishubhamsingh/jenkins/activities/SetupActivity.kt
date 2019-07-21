package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.databinding.ActivitySetupBinding
import com.ishubhamsingh.jenkins.fragments.InstanceSetupFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper


class SetupActivity : AppCompatActivity() {

    lateinit var mBinding: ActivitySetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setup)
        initFragment()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    private fun initFragment() {
        val fragment = InstanceSetupFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, fragment)
        ft.commit()
    }
}
