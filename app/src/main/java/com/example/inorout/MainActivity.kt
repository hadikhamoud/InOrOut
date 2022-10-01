package com.example.inorout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class MainActivity : AppCompatActivity() {

    private val myUrl = "http://10.0.2.2:8000/"
    var email: EditText? = null
    var password: EditText? = null
    private lateinit var sessionAuthenticator: SessionAuthenticator
    private lateinit var responseManager: ResponseManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        password = findViewById<EditText>(R.id.editTextTextPassword)
        sessionAuthenticator = SessionAuthenticator(this)
        responseManager = ResponseManager(this)


    }



    public final fun onClickSignIn(view: View): Unit {



        var status: TextView = findViewById(R.id.signInStatus)


        if(TextUtils.isEmpty(email?.text)) email?.error = "Required!"
        else if(TextUtils.isEmpty(password?.text)) password?.error = "Required!"
        else {

            // Create Service
            val service = retrofit.create(SimpleService::class.java)
            val fields: HashMap<String?, RequestBody?> = HashMap()
            fields["username"] =
                (email?.text.toString()).toRequestBody("text/plain".toMediaTypeOrNull())
            fields["password"] =
                (password?.text.toString()).toRequestBody("text/plain".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.signIn(fields)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        sessionAuthenticator.saveAccessToken(response.body())
                        val intent = Intent(this@MainActivity, InsideAppActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        status.text = responseManager.getResponseErrorDetails(response.errorBody())
                    }
                }
            }
        }

        }
        public fun onClickSignUpActivity(view: View): Unit {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("email", email?.text.toString())
            startActivity(intent)
        }
    public fun onClickBypassActivity(view: View): Unit {
        val intent = Intent(this, InsideAppActivity::class.java)
        startActivity(intent)

    }
}


