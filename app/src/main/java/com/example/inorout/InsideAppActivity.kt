package com.example.inorout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class InsideAppActivity : AppCompatActivity() {
    private val eventsFragment = EventsFragment()
    private val addEventFragment = AddEventFragment()
    private val accountFragment = AccountFragment()
    private val insideEventFragment = InsideEventFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = eventsFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_app)
        supportActionBar?.hide()


        setCurrentFragment(eventsFragment)


        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {
            when(it.itemId){
                R.id.events->{setCurrentFragment(eventsFragment)
                        true}
                R.id.addEvent->{setCurrentFragment(addEventFragment)
                    true}
                R.id.account->{setCurrentFragment(accountFragment)
                    true}
                else -> false
            }

        }

    }
    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
        addToBackStack(null)
            commit()
        }
}