package com.ishubhamsingh.jenkins.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ishubhamsingh.jenkins.R
import com.ishubhamsingh.jenkins.models.Job
import com.ishubhamsingh.jenkins.utils.JobHelper

class JobListAdapter(private val job: ArrayList<Job>?,val context: Context) : RecyclerView.Adapter<JobListAdapter.ViewHolder>() {

    private var util = JobHelper()


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.job_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.jobTitle.text = job!![i].name
        viewHolder.jobIcon.setImageResource(util.getCardIcon(job[i].color))
        viewHolder.jobCard.setCardBackgroundColor(util.getCardColor(job[i].color,context))

    }

    override fun getItemCount(): Int {
        return job!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var jobTitle:TextView = itemView.findViewById(R.id.jobTitle)
        var jobCard:CardView = itemView.findViewById(R.id.jobCard)
        var jobIcon:ImageView = itemView.findViewById(R.id.jobIcon)

    }

}