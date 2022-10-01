package com.example.inorout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyEventsActivity : AppCompatActivity() {
    private lateinit var sessionAuthenticator: SessionAuthenticator
    private lateinit var eventsList: ArrayList<EventCard>
    var adapter: EventCardsAdapter? = null
    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        supportActionBar?.hide()

        progressBar = findViewById(R.id.progressbar)

        sessionAuthenticator = SessionAuthenticator(this)
        listView = findViewById<ListView>(R.id.list_cards)
        getEvents()

        listView.setOnItemClickListener { _, _, position, _ ->
            val givenEvent = adapter?.getItem(position)
            Log.e("given event inside EventsFragment",givenEvent.toString())
            var intent: Intent = Intent(this, EventApplicantsActivity::class.java)
            intent.putExtra("extra_Event_Id",givenEvent?.Id)
            startActivity(intent)

        }
    }
    private fun getEvents(): Unit {
        progressBar.visibility= View.VISIBLE
        val service = retrofit.create(SimpleService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getMyCommunityEvents(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}")
            withContext(Dispatchers.Main) {

                eventsList = ArrayList(response)
                updateUi()
            }


        }
    }

    private fun updateUi(): Unit{
        progressBar.visibility= View.INVISIBLE
        adapter = EventCardsAdapter(this, eventsList)
        listView.adapter = adapter

        if (eventsList.size == 0) {
            emptyTextView = findViewById(R.id.empty)
            listView.emptyView = emptyTextView
        }

    }


}