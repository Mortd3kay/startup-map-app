package com.skyletto.startappfrontend.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.ui.main.fragments.MapsFragment
import com.skyletto.startappfrontend.ui.main.fragments.ProfileFragment.Companion.newInstance
import com.skyletto.startappfrontend.ui.start.StartActivity

class MainActivity : AppCompatActivity(), ActivityFragmentChanger {
    private lateinit var fm: FragmentManager
    private val profileFragment: Fragment = newInstance()
    private lateinit var mapFragment: MapsFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bundle = intent.extras
        var token = bundle?.getString("token")
        //var id = bundle?.getLong("id")
        if (token == null) {
            val sp = getSharedPreferences("profile", MODE_PRIVATE)
            token = sp.getString("token", "")
            if (token == "") {
                startActivity(Intent(this@MainActivity, StartActivity::class.java))
                finish()
            }
            //id = sp.getLong("id", -1)
        }
        fm = supportFragmentManager
        mapFragment = fm.fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), MapsFragment::class.java.name) as MapsFragment
        mapFragment.setActivity(this)
        fm.beginTransaction().replace(R.id.main_pane, mapFragment).commitNow()
    }

    override fun goToProfile() {
        fm.beginTransaction().replace(R.id.main_pane, profileFragment).addToBackStack("profile").commit()
    }

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
}