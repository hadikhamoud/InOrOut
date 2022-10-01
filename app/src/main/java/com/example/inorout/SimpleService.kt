package com.example.inorout

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface SimpleService {
    @Multipart
    @POST("token")
    suspend fun signIn(@PartMap map: HashMap<String?, RequestBody?>) : Response<ResponseBody>

    @POST("users")
    suspend fun signUp(@Body user: User): Response<ResponseBody>

    @Multipart
    @POST("usersImage")
    suspend fun signUpImage(@Part image: MultipartBody.Part, @PartMap userAdd: HashMap<String?, RequestBody?>):Response<ResponseBody>


    @GET("communityevents/")
    suspend fun getCommunityEvents(@Header("Authorization") token: String?): List<EventCard>

    @GET("communityevents/this")
    suspend fun getCommunityEvent(@Header("Authorization") token: String?,@Query("event_id") event_id: Int?): EventCard

    @GET("communityevents/me")
    suspend fun getMyCommunityEvents(@Header("Authorization") token: String?): List<EventCard>

    @GET("applicants")
    suspend fun getApplicants(@Header("Authorization") token: String?,@Query("event_id") event_id: Int?): List<EventApplicant>

    @GET("applicants/this")
    suspend fun getApplicant(@Header("Authorization") token: String?,@Query("event_id") event_id: Int?,@Query("applicant_id") applicant_id: Int?): EventApplicant


    @GET("potentialevents")
    suspend fun getPotentialEvents(@Header("Authorization") token: String?): List<PotentialEvent>


    @POST("sendacceptance")
    suspend fun sendAcceptance(@Header("Authorization") token: String?,@Body acceptedApplicant: InterestedApplicant): Response<ResponseBody>

    @Multipart
    @POST("eventcreate")
    suspend fun eventCreate(@Header("Authorization") token: String?, @Part image: MultipartBody.Part, @PartMap eventAdd: HashMap<String?, RequestBody?>):Response<ResponseBody>


    @POST("applicantinterest")
    suspend fun applicantinterest(@Header("Authorization") token: String?,@Body interestedApplicant: InterestedApplicant):Response<ResponseBody>


}

//const val myUrl: String = "http://10.0.2.2:8000/"
const val myUrl: String = "https://inoroutapp.herokuapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(myUrl)
    .build()

object API {
    val retrofitService : SimpleService by lazy {
        retrofit.create(SimpleService::class.java)
    }
}

@ToJson
fun arrayListToJson(list: java.util.ArrayList<EventCard>) : List<EventCard> = list

@FromJson
fun arrayListFromJson(list: List<EventCard>) : java.util.ArrayList<EventCard> =
    java.util.ArrayList(list)