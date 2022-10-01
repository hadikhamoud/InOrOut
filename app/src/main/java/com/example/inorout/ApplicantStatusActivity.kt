package com.example.inorout

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.imageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApplicantStatusActivity : AppCompatActivity() {
    lateinit var fullName: TextView
    lateinit var city: TextView
    lateinit var age: TextView
    lateinit var gender: TextView
    lateinit var bio: TextView
    lateinit var eventBio: TextView
    lateinit var socialMedia: TextView
    lateinit var socialMediaTitle: TextView
    lateinit var acceptApplicant: Button
    lateinit var profileImage: ImageView
    var givenEventId: Int? = null
    var givenApplicantId: Int? = null
    lateinit var progressBar: ProgressBar
    private lateinit var sessionAuthenticator: SessionAuthenticator
    lateinit var cardView: CardView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicant_status)
        supportActionBar?.hide()
        fullName=findViewById(R.id.info_Full_Name)
        city=findViewById(R.id.info_City)
        age = findViewById(R.id.info_Age)
        gender = findViewById(R.id.info_Gender)
        bio = findViewById(R.id.info_Bio)
        eventBio = findViewById(R.id.info_Bio_about_this_event)
        socialMedia = findViewById(R.id.info_socials_platform_and_Code)
        acceptApplicant = findViewById(R.id.accept_applicant_button)
        profileImage = findViewById(R.id.imageViewProfilePicture)

        socialMediaTitle = findViewById(R.id.info_socials_title)
        progressBar = findViewById(R.id.progressbar)
        cardView = findViewById(R.id.card_view)
        sessionAuthenticator = SessionAuthenticator(this)
        val intent = intent
        givenEventId = intent.getIntExtra("extra_Event_Id",R.drawable.loading)
        givenApplicantId = intent.getIntExtra("extra_Applicant_Id",R.drawable.loading)
        getApplicantDetails()

        acceptApplicant.setOnClickListener{
            sendAcceptance()
        }



    }

    fun getApplicantDetails(){
        val service = retrofit.create(SimpleService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getApplicant(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}",givenEventId,givenApplicantId)
            withContext(Dispatchers.Main){
                createUi(response)
            }
        }

    }

    fun createUi(givenApplicant: EventApplicant){

        progressBar.visibility=View.INVISIBLE
        fullName.text=givenApplicant.fullName
        city.text= givenApplicant.city
        age.text = givenApplicant.getAge()
        gender.text = givenApplicant.getGender()
        bio.text = givenApplicant.bio
        eventBio.text = givenApplicant.userEventDescription
        socialMedia.text = "${givenApplicant.getPlatform()}: ${givenApplicant.socialMediaHandle}"
        if (givenApplicant.userStatus==1) {
            socialMedia.visibility = View.VISIBLE
            socialMediaTitle.visibility = View.VISIBLE
            acceptApplicant.isEnabled = false
        }
        if (givenApplicant.image!=null) bindImage(profileImage,givenApplicant.image)
        cardView.visibility = View.VISIBLE
        acceptApplicant.visibility = View.VISIBLE
    }

    fun sendAcceptance(){
        progressBar.visibility=View.VISIBLE
        val service = retrofit.create(SimpleService::class.java)
        val acceptedApplicant = InterestedApplicant(givenEventId,givenApplicantId,null)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.sendAcceptance(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}",acceptedApplicant)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    progressBar.visibility=View.INVISIBLE
                    socialMedia.visibility= View.VISIBLE
                    socialMediaTitle.visibility = View.VISIBLE
                    acceptApplicant.isEnabled=false
                }
                else{
                    Toast.makeText(this@ApplicantStatusActivity, "Error Accepting", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @BindingAdapter("imageUrl")
    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView)
                .load(imgUri)
                .apply(RequestOptions().placeholder(R.drawable.loading).error(R.drawable.loading))
                .into(imgView)
        }
    }
}