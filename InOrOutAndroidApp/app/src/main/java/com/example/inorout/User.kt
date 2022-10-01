package com.example.inorout

import androidx.lifecycle.ViewModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@JsonClass(generateAdapter = true)
data class User(
    @Json(name="email")
    val email: String,
    @Json(name="password")
    val password: String,
    @Json(name = "fullName")
    val fullName: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "gender")
    val gender: Int,
    @Json(name = "dob")
    val dob: String,
    @Json(name = "bio")
    val bio: String,
    @Json(name = "socialMedia")
    val socialMedia: Int,
    @Json(name = "socialMediaHandle")
    val socialMediaHandle: String,
    @Json(name = "image")
    val image: String?
): apiUtilities{
    override fun toPartMap(): HashMap<String?, RequestBody?> {
        val fields: HashMap<String?, RequestBody?> = HashMap()
        fields["email"] = email.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["password"] = password.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["fullName"] = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["city"] = city.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["country"] = country.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["gender"] = gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        fields["dob"] = dob.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["bio"] = bio.toRequestBody("text/plain".toMediaTypeOrNull())
        fields["socialMedia"] = socialMedia.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        fields["socialMediaHandle"] = socialMediaHandle.toString().toRequestBody("text/plain".toMediaTypeOrNull())



        return fields
    }
}


