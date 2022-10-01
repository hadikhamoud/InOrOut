package com.example.inorout

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    lateinit var fullName: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var city: EditText
    lateinit var country: EditText
    lateinit var gender: Spinner
    lateinit var dob: TextView
    lateinit var bio: EditText
    lateinit var socialMedia: Spinner
    lateinit var socialMediaHandle: EditText
    lateinit var addImageText: TextView
    lateinit var addImageButton: Button
    lateinit var responseManager: ResponseManager
    var selectedImage: Uri? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        responseManager =  ResponseManager(this)

        fullName = findViewById(R.id.editTextFulLName)
        email = findViewById(R.id.editTextTextEmailAddressSignUp)
        password = findViewById(R.id.editTextTextPasswordSignUp)
        city = findViewById(R.id.editTextCity)
        country = findViewById(R.id.editTextCountry)
        gender = findViewById(R.id.spinnerGender)
        dob = findViewById(R.id.dobTextEdit)
        bio = findViewById(R.id.editTextBio)
        socialMedia = findViewById(R.id.EditTextSocialMediaPlatform)
        socialMediaHandle = findViewById(R.id.EditTextSocialMediaUsername)
        addImageText = findViewById(R.id.addImageTextView)


        dob = findViewById(R.id.dobTextEdit)
        dob.setOnClickListener{
            val calendar = Calendar.getInstance()
            var datePickerListener = DatePickerDialog.OnDateSetListener { DatePicker, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_YEAR, day)
                dob.text = SimpleDateFormat("YYYY-MM-DD").format(calendar.time)
            }
            DatePickerDialog(this,datePickerListener,calendar.get(Calendar.YEAR),calendar.get(
                Calendar.MONTH),calendar.get(Calendar.DAY_OF_YEAR)).show()

        }

        addImageButton = findViewById(R.id.addImageButton)

        addImageButton.setOnClickListener{
            openImageSelector()
        }


        val inputEmail = intent.getStringExtra("email")
        email?.setText(inputEmail)







    }

    private fun genderSpinnerValue(genderSpinner: Spinner):Int{
        val value = genderSpinner.selectedItem.toString()
        if (value=="Male") return 1
        else if(value=="Female") return 2
        return 0
    }

    //0: Instagram
    //1: Facebook
    //2: Snapchat

    private fun socialMediaSpinnerValue(socialMediaSpinner: Spinner):Int{
        val value = socialMediaSpinner.selectedItem.toString()
        if (value=="Instagram") return 0
        else if(value=="Facebook") return 1
        return 2
    }


    public fun onClickUploadImage(view: View): Unit{

    }

    public fun onClickSignUp(view: View): Unit{

        if(TextUtils.isEmpty(fullName.text)) fullName.error = "Required!"
        else if(TextUtils.isEmpty(email.text)) email.error = "Required!"
        else if(TextUtils.isEmpty(password.text)) password.error = "Required!"
        else if(TextUtils.isEmpty(city.text)) city.error = "Required!"
        else if(TextUtils.isEmpty(country.text)) country.error = "Required!"
        else if(TextUtils.isEmpty(bio.text)) bio.error = "Required!"
        else if(TextUtils.isEmpty(socialMediaHandle.text)) socialMediaHandle.error = "Required!"
        else {


            val service = retrofit.create(SimpleService::class.java)


            val fileResolver = contentResolver?.openFileDescriptor(selectedImage!!, "r")
            val file = File(cacheDir, contentResolver?.getFileName(selectedImage!!))
            val inputStream = FileInputStream(fileResolver?.fileDescriptor)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            var signUpUser = User(
                fullName = fullName?.text.toString(),
                email = email?.text.toString(),
                password = password?.text.toString(),
                city = city?.text.toString(),
                country = country?.text.toString(),
                gender = genderSpinnerValue(genderSpinner = gender),
                dob = dob.text.toString(),
                bio = bio?.text.toString(),
                socialMedia = socialMediaSpinnerValue(socialMedia),
                socialMediaHandle = socialMediaHandle?.text.toString(),
                image = null
            )

            val signUpUserMap = signUpUser.toPartMap()


            CoroutineScope(Dispatchers.IO).launch {
                val body = MultipartBody.Part.createFormData(
                    "image", "userImage",
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
                val response = API.retrofitService.signUpImage(body, signUpUserMap)
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "sign up successful!", LENGTH_SHORT)

                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        var error = responseManager.getResponseErrorDetails(response.errorBody())
                        Toast.makeText(this@SignUpActivity, "Email already taken", Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }
        }

    }
    private fun openImageSelector() {
        Intent(Intent.ACTION_PICK).also{
            it.type="image/*"
            startActivityForResult(it,100)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                100-> {
                    selectedImage=data?.data
                    addImageText.text = "Image Selected!"

                }

            }
        }
    }
}