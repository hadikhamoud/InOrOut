package com.example.inorout

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InterestedApplicant(
    @Json(name = "event_id")
    val eventId: Int?,
    @Json(name = "user_id")
    val userId: Int?,
    @Json(name="user_event_description")
    val userEventDescription: String?,
){
    constructor(eventId: Int?, userEventDescription: String?):
            this(
                eventId,
                null,
                userEventDescription,
            ){}

}
