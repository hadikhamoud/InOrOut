package com.example.inorout

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import okhttp3.ResponseBody

class SessionAuthenticator (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    fun saveAccessToken(responseBody: ResponseBody?) {
        if (responseBody==null) return

        val gson = Gson()
        val convertedMap = gson.fromJson(responseBody.string(),HashMap::class.java)
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, convertedMap["access_token"] as String?)
        editor.apply()
    }

    fun fetchAccessToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun deleteAccessToken(): Unit{
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }


}

