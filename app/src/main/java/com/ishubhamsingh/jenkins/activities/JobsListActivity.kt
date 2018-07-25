package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishubhamsingh.jenkins.Constants
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.adapters.JobListAdapter
import com.ishubhamsingh.jenkins.interfaces.RequestInterface
import com.ishubhamsingh.jenkins.models.Home
import com.ishubhamsingh.jenkins.models.Job
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_jobslist.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.lang.Thread.sleep

class JobsListActivity: AppCompatActivity(),AnkoLogger {

    private lateinit var prefJenkins: SharedPreferences
    private lateinit var prefAccount: SharedPreferences
    private lateinit var mCompositeDisposable: CompositeDisposable
    private var data:ArrayList<Job>? = null
    private lateinit var adapter: JobListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobslist)

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Rubik-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        toolbar.title = getString(R.string.jobs)

        mCompositeDisposable = CompositeDisposable()
        prefAccount=getSharedPreferences(Constants.PREFS_ACCOUNT, Context.MODE_PRIVATE)
        prefJenkins=getSharedPreferences(Constants.PREFS_JENKINS_DETAILS, Context.MODE_PRIVATE)

        bt_retry.setOnClickListener{
            fetchList()
        }

        fetchList()
    }

    private fun fetchList() {

        error_view.visibility = View.GONE
        joblist_progress_bar.smoothToShow()

        val requestInterface = Retrofit.Builder()
                .baseUrl(prefJenkins.getString(Constants.KEY_URL,"url"))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        mCompositeDisposable.add(requestInterface.getResultAuth(prefAccount.getString(Constants.KEY_AUTH_KEY,"default"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))


    }

    private fun handleResponse(result: Result<Home>) {

        data = result.response()!!.body()!!.jobs
        val totalJobs:Int = data!!.size
        val runningJobs:Int = data!!.filter { job -> job.color == "blue_anime" || job.color == "red_anime" }.size
        val successfulJobs:Int = data!!.filter { job -> job.color == "blue" }.size
        val failedJobs:Int = data!!.filter { job -> job.color == "red"}.size
        val unstableJobs:Int = data!!.filter { job -> job.color == "yellow" }.size
        val abortedJobs:Int = data!!.filter { job -> job.color == "aborted" }.size
        tv_total_jobs.text = getString(R.string.total_jobs ,totalJobs.toString())
        tv_running_jobs.text = getString(R.string.running_jobs ,runningJobs.toString())
        tv_success_jobs.text = getString(R.string.successful_jobs, successfulJobs.toString())
        tv_failed_jobs.text = getString(R.string.failed_jobs, failedJobs.toString())
        tv_unstable_jobs.text = getString(R.string.unstable_jobs, unstableJobs.toString())
        tv_aborted_jobs.text = getString(R.string.aborted_jobs, abortedJobs.toString())
        adapter = JobListAdapter(data,this)
        rv_joblist.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rv_joblist.layoutManager = layoutManager
        joblist_progress_bar.smoothToHide()
        rv_joblist.visibility = View.VISIBLE
        jobs_info_card.visibility = View.VISIBLE
        rv_joblist.adapter = adapter


    }

    private fun handleError(error:Throwable) {
        joblist_progress_bar.smoothToHide()
        error_view.visibility = View.VISIBLE


    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}