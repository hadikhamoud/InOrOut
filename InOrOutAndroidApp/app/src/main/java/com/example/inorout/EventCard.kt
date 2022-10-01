package com.example.inorout

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@JsonClass(generateAdapter = true)
data class EventCard(
    @Json(name="name")
    val name: String,
    @Json(name = "type")
    val type: String,
    @Json(name="description")
    val description: String,
    @Json(name="occurence")
    val occurence: String?,
    @Json(name="user_owner_id")
    val userOwnerId: Int?,
    @Json(name="user_full_name")
    val userFullName: String?,
    @Json(name="num_Of_Applicants_needed")
    val num_OfApplicantsNeeded: Int?,
    @Json(name="status")
    val status: Boolean?,
    @Json(name="id")
    val Id: Int?,
    @Json(name="images")
    val images: List<EventImage>?

){
    fun occurenceFormat(dateTime: Boolean): String{
        if (occurence==null) return ""
        val localDateTime: LocalDateTime = LocalDateTime.parse(occurence)
        val formatter: DateTimeFormatter = if(dateTime) DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")  else DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val output: String = formatter.format(localDateTime)
        return output
    }
}





//ownerName: String, ownerId: Int
//private var status: Boolean = true
//private var numOfApplicants: Int = 0