package com.example.inorout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PotentialEventsAdapter(context: Context, potentialEvents: ArrayList<PotentialEvent>): BaseAdapter() {
    private val context = context
    private val potentialEvents = potentialEvents
    private lateinit var owner_name: TextView
    private lateinit var name: TextView
    private lateinit var status: TextView


    override fun getCount(): Int {
       return potentialEvents.count()
    }

    override fun getItem(p0: Int): PotentialEvent {
        return potentialEvents[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 10000010000
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.potential_events_layout,parent,false)
        owner_name = convertView.findViewById(R.id.info_Event_Status_Owner)
        name = convertView.findViewById(R.id.info_Event_Status_Title)
        status = convertView.findViewById(R.id.info_Status)



        var givenPotentialEventCard = getItem(position)

        owner_name.text = givenPotentialEventCard.userFullName
        name.text = givenPotentialEventCard.name
        status.text = givenPotentialEventCard.getStatus()

        if(givenPotentialEventCard.userStatus==1) status.setTextColor(R.color.green)


        return convertView
    }
}