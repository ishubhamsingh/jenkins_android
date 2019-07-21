package com.ishubhamsingh.jenkins.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Created by Shubham Singh on 14-07-2019.
 */

class Preferences(val context: Context) {

    fun storeString(key:String, data:String?) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit()
            .putString(key,data)
            .apply()

    }

    fun fetchString(key:String):String {
        val preferences:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key,"") ?: ""
    }

    fun storeBoolean(key:String, data:Boolean) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit()
            .putBoolean(key,data)
            .apply()

    }

    fun fetchBoolean(key:String):Boolean {
        val preferences:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(key,false)
    }

    fun clearPreferences(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().clear().apply()
    }
}