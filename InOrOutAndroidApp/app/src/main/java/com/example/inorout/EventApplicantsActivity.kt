package com.example.inorout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventApplicantsActivity : AppCompatActivity() {
    var givenEventId: Int? = null
    private lateinit var sessionAuthenticator: SessionAuthenticator
    private lateinit var applicantsList: ArrayList<EventApplicant>
    var adapter: EventApplicantsAdapter? = null
    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_applicants)
        supportActionBar?.hide()
        val intent = intent
        givenEventId = intent.getIntExtra("extra_Event_Id",R.drawable.loading)
        sessionAuthenticator = SessionAuthenticator(this)
        listView = findViewById(R.id.list_applicants)
        getApplicants()
        listView.setOnItemClickListener { _, _, position, _ ->
            val givenApplicant = adapter?.getItem(position)
            Log.e("given event inside EventsFragment",givenApplicant.toString())
            val intent: Intent = Intent(this, ApplicantStatusActivity::class.java)
            intent.putExtra("extra_Event_Id",givenEventId)
            intent.putExtra("extra_Applicant_Id",givenApplicant?.Id)
            startActivity(intent)

        }

    }
    private fun getApplicants(): Unit {
        val service = retrofit.create(SimpleService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getApplicants(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}",givenEventId)
            withContext(Dispatchers.Main) {

                applicantsList = ArrayList(response)
                updateUi()


            }


        }
    }

    private fun updateUi(): Unit{

        adapter = EventApplicantsAdapter(this, applicantsList)
        listView.adapter = adapter

        if (applicantsList.size == 0) {
            emptyTextView = findViewById(R.id.emptyApplicants)
            listView.emptyView = emptyTextView
        }

    }
}