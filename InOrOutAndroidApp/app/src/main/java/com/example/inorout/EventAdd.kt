package com.example.inorout

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.reflect.full.declaredMemberProperties

@JsonClass(generateAdapter = true)
data class EventAdd(
    @Json(name="name")
    val name: String,
    @Json(name = "type")
    val type: String,
    @Json(name="description")
    val description: String,
    @Json(name="num_Of_Applicants_needed")
    val numOfApplicantsNeeded: Int?,
    @Json(name="occurence")
    val occurence: String?
): apiUtilities {
    override fun toPartMap(): HashMap<String?, RequestBody?> {
        val fields: HashMap<String?, RequestBody?> = HashMap()
        fields["name"] = name.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["type"] = type.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["description"] = description.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["num_Of_Applicants_needed"] =
            numOfApplicantsNeeded.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        fields["occurence"] = occurence?.toRequestBody("text/plain".toMediaTypeOrNull())
        return fields
    }
}