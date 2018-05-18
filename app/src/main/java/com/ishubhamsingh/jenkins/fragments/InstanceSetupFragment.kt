package com.ishubhamsingh.jenkins.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.interfaces.RequestInterface
import com.ishubhamsingh.jenkins.models.Home
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_setup.*
import kotlinx.android.synthetic.main.fragment_instance_setup.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


class InstanceSetupFragment : Fragment(), AnkoLogger {

    private lateinit var mCompositeDisposable: CompositeDisposable


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instance_setup, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCompositeDisposable = CompositeDisposable()

        activity.tv_title.text = getString(R.string.setup)
        activity.tv_desc.text = getString(R.string.setup_desc)

        bt_proceed.setOnClickListener {

            hideError()

            setupProceed(et_name.text.toString(), et_url.text.toString())
        }
    }


    private fun setupProceed(name:String, url:String){

        progress_bar.smoothToShow()


        if(!name.isEmpty() && !url.isEmpty()){

            if(isValid(url)){

                val requestInterface = Retrofit.Builder()
                        .baseUrl(url)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(RequestInterface::class.java)

                mCompositeDisposable.add(requestInterface.getResult()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError))

            } else {

                showError(getString(R.string.error_invalid_url))
                progress_bar.smoothToHide()

            }

        } else {

            showError(getString(R.string.error_empty))
            progress_bar.smoothToHide()

        }

    }

    private fun handleResponse(result: Result<Home>) {

        val code:Int = result.response()!!.code()
        val jenkinsVersion = result.response()!!.headers().get("X-Jenkins")
        if (jenkinsVersion != null) {

            gotToAuthentication(code,jenkinsVersion,et_name.text.toString(),et_url.text.toString())

        } else {
            showError(getString(R.string.invalid_jenkins_instance))
            progress_bar.smoothToHide()
        }

    }

    private fun handleError(error:Throwable) {

        showError(getString(R.string.error_invalid_url))
        progress_bar.smoothToHide()

    }

    private fun gotToAuthentication(code:Int,jenkinsVersion:String,name: String,url: String) {

        val bundle = Bundle()
        bundle.putString(Constants.KEY_JENKINS_VERSION,jenkinsVersion)
        bundle.putInt(Constants.KEY_CODE,code)
        bundle.putString(Constants.KEY_NAME,name)
        bundle.putString(Constants.KEY_URL,url)

        progress_bar.smoothToHide()

        val fragment: Fragment = AuthenticationFragment()
        fragment.arguments = bundle
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, fragment)
        ft.commit()

    }

    private fun showError(errorMsg:String) {

        tv_error.text=errorMsg
        tv_error.visibility = View.VISIBLE

    }

    private fun hideError() {
        tv_error.text=""
        tv_error.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
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


}
