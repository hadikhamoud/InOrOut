package com.example.inorout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class AccountFragment : Fragment() {
    var fragmentView: View? = null
    lateinit var potentialEvents: CardView
    lateinit var myEvents: CardView
    lateinit var signOut: CardView
    lateinit var sessionAuthenticator: SessionAuthenticator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        if (fragmentView != null) {
            return fragmentView
        }

        val view: View = inflater.inflate(R.layout.fragment_account, container, false)
        potentialEvents = view.findViewById(R.id.card_view_Applied_events)
        myEvents = view.findViewById(R.id.card_view_my_events)
        signOut = view.findViewById(R.id.card_view_sign_out)
        sessionAuthenticator = SessionAuthenticator(requireContext())

        potentialEvents.setOnClickListener{
            val intent=  Intent(requireActivity(),PotentialEventsActivity::class.java)
            startActivity(intent)
        }
        myEvents.setOnClickListener{
            val intent=  Intent(requireActivity(),MyEventsActivity::class.java)
            startActivity(intent)
        }
        signOut.setOnClickListener{
            val intent =  Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
            sessionAuthenticator.deleteAccessToken()

            activity?.finishAffinity()
        }



        fragmentView = view
        return view
    }


}