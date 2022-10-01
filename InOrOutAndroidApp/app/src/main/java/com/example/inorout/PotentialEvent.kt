package com.example.inorout

import android.widget.TextView
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.w3c.dom.Text

@JsonClass(generateAdapter = true)
data class PotentialEvent(
    @Json(name="user_full_name")
    val userFullName: String,
    @Json(name="name")
    val name: String,
    @Json(name="user_status")
    val userStatus: Int,

    ){

    fun getStatus():String{
        if(userStatus==0) return "Pending"
        if(userStatus==1) return "Accepted"
        return "Rejected"
    }

    fun setStatusColor(statusTextView: TextView): Unit{
        if(userStatus==1) statusTextView.setTextColor(R.color.green)
        if(userStatus==2) statusTextView.setTextColor(R.color.red)
    }
}
