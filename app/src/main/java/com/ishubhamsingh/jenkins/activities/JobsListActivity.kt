package com.ishubhamsingh.jenkins.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.adapters.JobListAdapter
import com.ishubhamsingh.jenkins.core.exception.Failure
import com.ishubhamsingh.jenkins.core.extension.failure
import com.ishubhamsingh.jenkins.core.extension.observe
import com.ishubhamsingh.jenkins.core.extension.viewModel
import com.ishubhamsingh.jenkins.core.viewmodel.ViewModelFactory
import com.ishubhamsingh.jenkins.databinding.ActivityJobslistBinding
import com.ishubhamsingh.jenkins.models.Home
import com.ishubhamsingh.jenkins.models.Job
import com.ishubhamsingh.jenkins.viewmodels.JobListViewModel
import dagger.android.AndroidInjection
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

class JobsListActivity: AppCompatActivity(),AnkoLogger {

    private lateinit var mBinding: ActivityJobslistBinding

    private var data:ArrayList<Job>? = null
    private lateinit var adapter: JobListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: JobListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_jobslist)

        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.jobs)

        mBinding.btRetry.setOnClickListener{
            onDataRefresh()
            fetchList()
        }

        mBinding.swipeRefresh.setOnRefreshListener{
            onDataRefresh()
            fetchList()
        }


        viewModel = viewModel(viewModelFactory) {
            observe(homeData, ::handleResponse)
            failure(failure, ::handleError)
        }

        fetchList()
    }

    private fun fetchList() {

        mBinding.errorView.visibility = View.GONE
        mBinding.joblistProgressBar.smoothToShow()

        viewModel.fetchJobsList()

    }

    private fun onDataRefresh() {
        mBinding.tvTotalJobs.text = getString(R.string.total_jobs ,"--")
        mBinding.tvRunningJobs.text = getString(R.string.running_jobs ,"--")
        mBinding.tvSuccessJobs.text = getString(R.string.successful_jobs, "--")
        mBinding.tvFailedJobs.text = getString(R.string.failed_jobs, "--")
        mBinding.tvUnstableJobs.text = getString(R.string.unstable_jobs, "--")
        mBinding.tvAbortedJobs.text = getString(R.string.aborted_jobs, "--")
        mBinding.rvJoblist.visibility = View.GONE
    }

    private fun handleResponse(result: Home?) {

        data = result?.jobs

        data?.let {
            mBinding.tvTotalJobs.text = getString(R.string.total_jobs ,it.size.toString())
            mBinding.tvRunningJobs.text = getString(R.string.running_jobs ,it.filter { job -> job.color == "blue_anime" || job.color == "red_anime" }.size.toString())
            mBinding.tvSuccessJobs.text = getString(R.string.successful_jobs, it.filter { job -> job.color == "blue" }.size.toString())
            mBinding.tvFailedJobs.text = getString(R.string.failed_jobs, it.filter { job -> job.color == "red"}.size.toString())
            mBinding.tvUnstableJobs.text = getString(R.string.unstable_jobs, it.filter { job -> job.color == "yellow" }.size.toString())
            mBinding.tvAbortedJobs.text = getString(R.string.aborted_jobs, it.filter { job -> job.color == "aborted" }.size.toString())
            adapter = JobListAdapter(it,this)
            mBinding.rvJoblist.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this)
            mBinding.rvJoblist.layoutManager = layoutManager
            mBinding.rvJoblist.visibility = View.VISIBLE
            mBinding.rvJoblist.adapter = adapter
        }

        hideProgress()
    }

    private fun handleError(failure: Failure?) {
        hideProgress()
        mBinding.errorView.visibility = View.VISIBLE


    }

    private fun hideProgress() {
        mBinding.joblistProgressBar.smoothToHide()
        if(mBinding.swipeRefresh.isRefreshing) mBinding.swipeRefresh.isRefreshing = false
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}