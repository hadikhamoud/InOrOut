package com.example.inorout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*



class EventsFragment : Fragment() {
    private lateinit var sessionAuthenticator: SessionAuthenticator
    private lateinit var eventsList: ArrayList<EventCard>
    private var adapter: EventCardsAdapter? = null
    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView
    private var fragmentView: View? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentView != null) {
            return fragmentView;
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_events, container, false)
        sessionAuthenticator = SessionAuthenticator(requireActivity())
        listView = view.findViewById<ListView>(R.id.list_cards)
        getEvents()
        listView.setOnItemClickListener { _, _, position, _ ->
            val givenEvent = adapter?.getItem(position)
            Log.e("given event inside EventsFragment",givenEvent.toString())
            val intent: Intent = Intent(requireContext(), InsideEventActivity::class.java)
            intent.putExtra("extra_Event_Id",givenEvent?.Id)
            startActivity(intent)

        }
        fragmentView = view
        return view

    }

       private fun getEvents(): Unit {
        val service = retrofit.create(SimpleService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getCommunityEvents(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}")
            withContext(Dispatchers.Main) {

                eventsList = ArrayList(response)
                updateUi()
            }


        }
    }

    private fun updateUi(): Unit{

        adapter = EventCardsAdapter(requireActivity(), eventsList)
        listView.adapter = adapter

        if (eventsList.size == 0) {
            emptyTextView = view!!.findViewById(R.id.empty)
            listView.emptyView = emptyTextView
        }

    }

}

