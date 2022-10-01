package com.example.inorout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PotentialEventsActivity : AppCompatActivity() {
    private lateinit var sessionAuthenticator: SessionAuthenticator
    private lateinit var eventsList: ArrayList<PotentialEvent>
    var adapter: PotentialEventsAdapter? = null
    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_potential_events)
        supportActionBar?.hide()


        progressBar = findViewById(R.id.progressbar)
        sessionAuthenticator = SessionAuthenticator(this)
        listView = findViewById<ListView>(R.id.list_potential_events)
        getPotentialEvents()
    }

    private fun getPotentialEvents(): Unit {
        progressBar.visibility=View.VISIBLE
        val service = retrofit.create(SimpleService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getPotentialEvents(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}")
            withContext(Dispatchers.Main) {

                eventsList = ArrayList(response)
                updateUi()
            }


        }
    }
    private fun updateUi(): Unit{
        progressBar.visibility=View.INVISIBLE

        adapter = PotentialEventsAdapter(this, eventsList)
        listView.adapter = adapter

        if (eventsList.size == 0) {
            emptyTextView = findViewById(R.id.empty)
            listView.emptyView = emptyTextView
        }

    }
}