package com.ishubhamsingh.jenkins.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.activities.SetupActivity
import com.ishubhamsingh.jenkins.core.exception.Failure
import com.ishubhamsingh.jenkins.core.extension.observe
import com.ishubhamsingh.jenkins.core.extension.viewModel
import com.ishubhamsingh.jenkins.core.viewmodel.ViewModelFactory
import com.ishubhamsingh.jenkins.data.Preferences
import com.ishubhamsingh.jenkins.databinding.FragmentInstanceSetupBinding
import com.ishubhamsingh.jenkins.models.Home
import dagger.android.support.AndroidSupportInjection
import okhttp3.Headers
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.net.URL
import javax.inject.Inject


class InstanceSetupFragment : Fragment(), AnkoLogger {

    private lateinit var mBinding: FragmentInstanceSetupBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: SetupViewmodel

    @Inject
    lateinit var preferences: Preferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        AndroidSupportInjection.inject(this)

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_instance_setup, container, false)

        viewModel = viewModel(viewModelFactory) {
            observe(jenkinsInfoData, ::handleResponse)
            observe(failure, ::handleError)
        }
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as SetupActivity).mBinding.tvTitle.text = getString(R.string.setup)
        (activity as SetupActivity).mBinding.tvDesc.text = getString(R.string.setup_desc)

        mBinding.btProceed.setOnClickListener {

            hideError()

            setupProceed(mBinding.etName.text.toString(), mBinding.etUrl.text.toString())
        }
    }


    private fun setupProceed(name:String, url:String){

        mBinding.progressBar.smoothToShow()


        if(name.isNotEmpty() && url.isNotEmpty()){

            if(isValid(url)){
                viewModel.getHomeResult(url)

            } else {

                showError(getString(R.string.error_invalid_url))
                mBinding.progressBar.smoothToHide()

            }

        } else {

            showError(getString(R.string.error_empty))
            mBinding.progressBar.smoothToHide()

        }

    }

    private fun handleResponse(result: Pair<Int, Headers>?) {

        val code:Int = result?.first ?: 0
        val jenkinsVersion = result?.second?.get("X-Jenkins")
        if (jenkinsVersion != null) {

            goToAuthentication(code,jenkinsVersion,mBinding.etName.text.toString(),mBinding.etUrl.text.toString())

        } else {
            showError(getString(R.string.invalid_jenkins_instance))
            mBinding.progressBar.smoothToHide()
        }

    }

    private fun handleError(failure: Failure?) {
        preferences.clearPreferences()
        showError(getString(R.string.error_invalid_url))
        mBinding.progressBar.smoothToHide()

    }

    private fun goToAuthentication(code:Int,jenkinsVersion:String,name: String,url: String) {

        val bundle = Bundle()
        bundle.putString(Constants.KEY_JENKINS_VERSION,jenkinsVersion)
        bundle.putInt(Constants.KEY_CODE,code)
        bundle.putString(Constants.KEY_NAME,name)
        bundle.putString(Constants.KEY_URL,url)

        mBinding.progressBar.smoothToHide()

        val fragment: Fragment = AuthenticationFragment()
        fragment.arguments = bundle
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fragment_frame, fragment)
        ft.commit()

    }

    private fun showError(errorMsg:String) {

        mBinding.tvError.text=errorMsg
        mBinding.tvError.visibility = View.VISIBLE

    }

    private fun hideError() {
        mBinding.tvError.text=""
        mBinding.tvError.visibility = View.GONE
    }

    private fun isValid(url: String): Boolean {

        return try {
            URL(url).toURI()
            true
        } catch (e: Exception) {
            info("1"+e.message)
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


}
