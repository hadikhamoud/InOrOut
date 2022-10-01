package com.example.inorout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class EventApplicantsAdapter(context: Context, applicants: ArrayList<EventApplicant>): BaseAdapter()  {
    private val context = context
    private val applicants = applicants
    private lateinit var imgResource: ImageView
    private lateinit var fullName: TextView
    private lateinit var age: TextView
    private lateinit var city: TextView
    private lateinit var gender: TextView




    override fun getCount(): Int {
        return applicants.count()
    }

    override fun getItem(p0: Int): EventApplicant {
        return applicants[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 1000000010
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.applicant_card_layout,parent,false)
        fullName = convertView.findViewById(R.id.info_Full_Name)
        age = convertView.findViewById(R.id.info_Age)
        city = convertView.findViewById(R.id.info_City)
        gender = convertView.findViewById(R.id.info_Gender)

        imgResource = convertView.findViewById(R.id.imageViewProfilePicture)

        var givenApplicantCard = getItem(position)

        fullName.text = givenApplicantCard.fullName
        age.text = givenApplicantCard.getAge()
        city.text = givenApplicantCard.city
        gender.text = givenApplicantCard.getGender()


        return convertView


    }


}
