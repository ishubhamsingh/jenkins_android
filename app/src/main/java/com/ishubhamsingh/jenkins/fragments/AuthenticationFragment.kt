package com.ishubhamsingh.jenkins.fragments

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.activities.DashboardActivity
import com.ishubhamsingh.jenkins.activities.SetupActivity
import com.ishubhamsingh.jenkins.core.exception.Failure
import com.ishubhamsingh.jenkins.core.extension.observe
import com.ishubhamsingh.jenkins.core.extension.viewModel
import com.ishubhamsingh.jenkins.core.viewmodel.ViewModelFactory
import com.ishubhamsingh.jenkins.data.Preferences
import com.ishubhamsingh.jenkins.databinding.FragmentAuthenticationBinding
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_authentication.*
import okhttp3.Headers
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import java.io.UnsupportedEncodingException
import javax.inject.Inject


class AuthenticationFragment : Fragment(), AnkoLogger {

    private lateinit var mBinding: FragmentAuthenticationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: SetupViewmodel

    @Inject
    lateinit var preferences: Preferences

    private lateinit var bundle: Bundle
    private lateinit var username: String
    private lateinit var password: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        AndroidSupportInjection.inject(this)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_authentication, container, false)

        viewModel = viewModel(viewModelFactory) {
            observe(jenkinsInfoData, ::handleResponse)
            observe(failure, ::handleError)
        }

        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.arguments?.let {
            bundle = it
        }

        (activity as SetupActivity).mBinding.tvDesc.text = getString(R.string.jenkins_version_desc,bundle.getString(Constants.KEY_JENKINS_VERSION)) + "\n" + getString(R.string.auth_desc)

        if(bundle.getInt(Constants.KEY_CODE) == Constants.TWO_ZERO_ZERO) { // Authentication optional
            mBinding.cbSkipAuth.visibility = View.VISIBLE
        } else if (bundle.getInt(Constants.KEY_CODE) == Constants.FOUR_ZERO_THREE) { // Authentication required
            mBinding.cbSkipAuth.isChecked = false
            mBinding.cbSkipAuth.visibility = View.GONE
        }

        mBinding.btBack.setOnClickListener {
            val fragment: Fragment = InstanceSetupFragment()
            fragment.arguments = bundle
            val ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.fragment_frame, fragment)
            ft?.commit()
        }

        mBinding.btStart.setOnClickListener {

            hideError()

            mBinding.authProgressBar.smoothToShow()

            if (mBinding.cbSkipAuth.isChecked) {

                gotoDashboard(false)

            } else {

                if (mBinding.etUsername.text.toString().isNotEmpty() && mBinding.etPassword.text.toString().isNotEmpty()) {
                    username = mBinding.etUsername.text.toString()
                    password = mBinding.etPassword.text.toString()

                    authenticate()

                } else {

                    showError(getString(R.string.error_empty))
                    mBinding.authProgressBar.smoothToHide()

                }
            }
        }
    }


    private fun authenticate() {

        mBinding.btStart.visibility = View.GONE
        mBinding.btBack.visibility = View.GONE

        preferences.storeString(Constants.KEY_AUTH_KEY, getAuthToken())
        preferences.storeBoolean(Constants.KEY_IS_AUTHORISED, true)
        viewModel.getHomeResult(bundle.getString(Constants.KEY_URL,""))
    }

    private fun handleResponse(result: Pair<Int, Headers>?) {

        val code:Int = result?.first ?: 0
        if (code == Constants.TWO_ZERO_ZERO) {

            gotoDashboard(true)


        } else if (code == Constants.FOUR_ZERO_ONE) {

            showError(getString(R.string.invalid_credentials))
            mBinding.btStart.visibility = View.VISIBLE
            mBinding.btBack.visibility = View.VISIBLE
            mBinding.authProgressBar.smoothToHide()

        }


    }

    private fun handleError(failure: Failure?) {
        mBinding.btStart.visibility = View.VISIBLE
        mBinding.btBack.visibility = View.VISIBLE
        mBinding.authProgressBar.smoothToHide()

    }

    private fun gotoDashboard(isAuthorised:Boolean) {

        preferences.storeString(Constants.KEY_NAME, bundle.getString(Constants.KEY_NAME) ?: "--")
        preferences.storeString(Constants.KEY_URL,bundle.getString(Constants.KEY_URL) ?: "--")
        preferences.storeString(Constants.KEY_JENKINS_VERSION,bundle.getString(Constants.KEY_JENKINS_VERSION) ?: "--")

        if (isAuthorised) {
            preferences.storeString(Constants.KEY_USERNAME, mBinding.etUsername.text.toString())
            preferences.storeString(Constants.KEY_AUTH_KEY, getAuthToken())
            preferences.storeBoolean(Constants.KEY_IS_AUTHORISED, true)
            preferences.storeBoolean(Constants.ACCOUNT_SETUP_DONE, true)
        } else {

            preferences.storeString(Constants.KEY_USERNAME, getString(R.string.anonymous))
            preferences.storeString(Constants.KEY_AUTH_KEY, null)
            preferences.storeBoolean(Constants.KEY_IS_AUTHORISED, false)
            preferences.storeBoolean(Constants.ACCOUNT_SETUP_DONE, true)
        }

        mBinding.authProgressBar.smoothToHide()

        activity?.startActivity<DashboardActivity>()
        activity?.finish()

    }

    private fun showError(errorMsg:String) {

        mBinding.tvAuthError.text=errorMsg
        mBinding.tvAuthError.visibility = View.VISIBLE

    }

    private fun hideError() {
        mBinding.tvAuthError.text=""
        mBinding.tvAuthError.visibility = View.GONE
    }

    private fun getAuthToken(): String {
        var data = ByteArray(0)
        try {
            data = ("$username:$password").toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }
}
