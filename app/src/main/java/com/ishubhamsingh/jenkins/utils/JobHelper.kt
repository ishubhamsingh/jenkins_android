package com.ishubhamsingh.jenkins.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.ishubhamsingh.jenkins.R

object JobHelper {

    fun getCardColor(color:String?,context:Context):Int {

       return when(color){
            "red" -> {
                ContextCompat.getColor(context, R.color.red)
            }

            "blue" -> {
                ContextCompat.getColor(context, R.color.green)
            }

            "yellow" -> {
               ContextCompat.getColor(context, R.color.primaryLightColor)

            }

            "aborted", "notbuilt" -> {
                ContextCompat.getColor(context, R.color.secondaryColor)

            }

            "red_anime" -> {
                ContextCompat.getColor(context, R.color.red)
            }

            else -> {
                ContextCompat.getColor(context, R.color.green)
            }
        }

    }

    fun getCardIcon(color: String?):Int {

        return when(color){
            "red" -> {
                 R.drawable.ic_cross
            }

            "blue" -> {

                R.drawable.ic_tick
            }

            "yellow" -> {

                R.drawable.ic_unstable_tri

            }

            "aborted","notbuilt" -> {

                R.drawable.ic_aborted
            }

            "red_anime" -> {
                R.drawable.ic_running
            }

            else -> {
                R.drawable.ic_running
            }

        }

    }
}