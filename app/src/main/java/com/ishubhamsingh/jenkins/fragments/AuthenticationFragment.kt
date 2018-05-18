package com.ishubhamsingh.jenkins.fragments

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.activities.DashboardActivity
import com.ishubhamsingh.jenkins.interfaces.RequestInterface
import com.ishubhamsingh.jenkins.models.Home
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_setup.*
import kotlinx.android.synthetic.main.fragment_authentication.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException


class AuthenticationFragment : Fragment(), AnkoLogger {

    private lateinit var prefJenkins: SharedPreferences
    private lateinit var prefAccount: SharedPreferences
    private lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var bundle: Bundle
    private lateinit var username: String
    private lateinit var password: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentication, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCompositeDisposable = CompositeDisposable()
        prefAccount=activity.getSharedPreferences(Constants.PREFS_ACCOUNT, Context.MODE_PRIVATE)
        prefJenkins=activity.getSharedPreferences(Constants.PREFS_JENKINS_DETAILS,Context.MODE_PRIVATE)
        bundle = this.arguments

        activity.tv_desc.text = getString(R.string.jenkins_version_desc,bundle.getString(Constants.KEY_JENKINS_VERSION)) + "\n" + getString(R.string.auth_desc)

        if(bundle.getInt(Constants.KEY_CODE) == Constants.TWO_ZERO_ZERO) { // Authentication optional
            cb_skip_auth.visibility = View.VISIBLE
        } else if (bundle.getInt(Constants.KEY_CODE) == Constants.FOUR_ZERO_THREE) { // Authentication required
            cb_skip_auth.isChecked = false
            cb_skip_auth.visibility = View.GONE
        }

        bt_start.setOnClickListener {

            hideError()

            auth_progress_bar.smoothToShow()

            if (cb_skip_auth.isChecked) {

                gotoDashboard(false)

            } else {

                if (!et_username.text.toString().isEmpty() && !et_password.text.toString().isEmpty()) {
                    username = et_username.text.toString()
                    password = et_password.text.toString()

                    authenticate()

                } else {

                    showError(getString(R.string.error_empty))
                    auth_progress_bar.smoothToHide()

                }
            }
        }
    }


    private fun authenticate() {

        val requestInterface = Retrofit.Builder()
                .baseUrl(bundle.getString(Constants.KEY_URL))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        mCompositeDisposable.add(requestInterface.getResultAuth(getAuthToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(result: Result<Home>) {

        val code:Int = result.response()!!.code()
        if (code == Constants.TWO_ZERO_ZERO) {

            gotoDashboard(true)


        } else if (code == Constants.FOUR_ZERO_ONE) {

            showError(getString(R.string.invalid_credentials))
            auth_progress_bar.smoothToHide()

        }


    }

    private fun handleError(error:Throwable) {

        info(error.localizedMessage)
        auth_progress_bar.smoothToHide()

    }

    private fun gotoDashboard(isAuthorised:Boolean) {

        prefJenkins.edit()
                .putString(Constants.KEY_URL,bundle.getString(Constants.KEY_URL))
                .putString(Constants.KEY_JENKINS_VERSION,bundle.getString(Constants.KEY_JENKINS_VERSION))
                .apply()

        if (isAuthorised) {
            prefAccount.edit()
                    .putString(Constants.KEY_NAME, bundle.getString(Constants.KEY_NAME))
                    .putString(Constants.KEY_USERNAME, et_username.text.toString())
                    .putString(Constants.KEY_AUTH_KEY, getAuthToken())
                    .putBoolean(Constants.KEY_IS_AUTHORISED, true)
                    .putBoolean(Constants.ACCOUNT_SETUP_DONE, true)
                    .apply()
        } else {

            prefAccount.edit()
                    .putString(Constants.KEY_NAME, bundle.getString(Constants.KEY_NAME))
                    .putString(Constants.KEY_USERNAME, getString(R.string.anonymous))
                    .putString(Constants.KEY_AUTH_KEY, null)
                    .putBoolean(Constants.KEY_IS_AUTHORISED, false)
                    .putBoolean(Constants.ACCOUNT_SETUP_DONE, true)
                    .apply()
        }

        auth_progress_bar.smoothToHide()

        startActivity<DashboardActivity>()
        activity.finish()

    }

    private fun showError(errorMsg:String) {

        tv_auth_error.text=errorMsg
        tv_auth_error.visibility = View.VISIBLE

    }

    private fun hideError() {
        tv_auth_error.text=""
        tv_auth_error.visibility = View.GONE
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
        mCompositeDisposable.clear()
    }

}
