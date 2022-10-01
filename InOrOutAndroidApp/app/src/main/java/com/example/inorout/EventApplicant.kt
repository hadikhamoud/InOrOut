package com.example.inorout

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class EventApplicant(
    @Json(name = "fullName")
    val fullName: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "dob")
    val dob: String,
    @Json(name = "gender")
    val gender: Int,
    @Json(name = "id")
    val Id: Int,
    @Json(name = "Country")
    val country: String?,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "socialMedia")
    val socialMedia: Int?,
    @Json(name = "socialMediaHandle")
    val socialMediaHandle: String?,
    @Json(name = "user_event_description")
    val userEventDescription: String?,
    @Json(name = "user_status")
    val userStatus: Int?,
    @Json(name = "image")
    val image: String?,

){
    constructor(fullName: String,city: String,dob: String,gender: Int, Id: Int):
            this(fullName,city,dob,gender,Id,null,null,null,null,null,null,null){}



    fun getGender(): String{
        if(gender==1) return "Male"
        else if(gender==2) return "Female"
        return "Other"
    }

    fun getAge(): String{
        val units = dob.split("-").toTypedArray()
        val dob: Calendar = Calendar.getInstance()
        val today: Calendar = Calendar.getInstance()
        dob.set(units[0].toInt(),units[1].toInt(),units[2].toInt())
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if((today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))) age = age--
        return age.toString()

    }

    fun getPlatform(): String{
        if(socialMedia==0) return "Instagram"
        else if(socialMedia==1) return "Facebook"
        return "Snapchat"
    }
}