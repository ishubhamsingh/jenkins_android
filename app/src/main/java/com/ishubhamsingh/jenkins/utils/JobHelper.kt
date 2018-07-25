package com.ishubhamsingh.jenkins.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.ishubhamsingh.jenkins.R

class JobHelper {

    fun getCardColor(color:String?,context:Context):Int {
        var cardColor:Int = ContextCompat.getColor(context, R.color.green)

        when(color){
            "red" -> {
                cardColor = ContextCompat.getColor(context, R.color.red)
            }

            "blue" -> {
                cardColor = ContextCompat.getColor(context, R.color.green)
            }

            "yellow" -> {
               cardColor = ContextCompat.getColor(context, R.color.primaryLightColor)

            }

            "aborted" -> {
                cardColor = ContextCompat.getColor(context, R.color.secondaryColor)

            }

            "notbuilt" -> {

                cardColor = ContextCompat.getColor(context, R.color.secondaryColor)
            }

            "red_anime" -> {
                cardColor = ContextCompat.getColor(context, R.color.red)
            }
        }

        return cardColor

    }

    fun getCardIcon(color: String?):Int {

        var cardIcon = R.drawable.ic_running

        when(color){
            "red" -> {
                cardIcon = R.drawable.ic_cross
            }

            "blue" -> {

                cardIcon =   R.drawable.ic_tick
            }

            "yellow" -> {

                cardIcon =  R.drawable.ic_unstable_tri

            }

            "aborted" -> {

                cardIcon =  R.drawable.ic_aborted
            }

            "notbuilt" -> {
                cardIcon =  R.drawable.ic_aborted
            }

            "red_anime" -> {
                cardIcon =  R.drawable.ic_running
            }

        }


        return cardIcon

    }
}