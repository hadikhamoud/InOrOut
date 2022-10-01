package com.example.inorout

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.google.gson.Gson
import okhttp3.ResponseBody

class ResponseManager(context: Context) {

    public fun getResponseAuthDetails(responseBody: ResponseBody?): String?{
        if(responseBody==null) return null
        val gson = Gson()
        val convertedMap = gson.fromJson(responseBody!!.string(),HashMap::class.java)
        return convertedMap["access_token"] as String
    }

    public fun getResponseErrorDetails(responseBody: ResponseBody?): String?{
        if(responseBody==null) return null
        val gson = Gson()
        val convertedMap = gson.fromJson(responseBody!!.string(),HashMap::class.java)
        return convertedMap["detail"] as String
    }

}

fun ContentResolver.getFileName(uri: Uri): String{
    var name = ""
    val cursor = query(uri,null,null,null,null)
    cursor?.use{
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}