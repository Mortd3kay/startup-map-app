package com.skyletto.startappfrontend.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.ui.main.fragments.MapsFragment
import com.skyletto.startappfrontend.ui.main.fragments.MessagesFragment
import com.skyletto.startappfrontend.ui.main.fragments.ProfileFragment
import com.skyletto.startappfrontend.ui.start.StartActivity
import java.util.*

class MainActivity : AppCompatActivity(), ActivityFragmentChanger {
    private lateinit var bnv: BottomNavigationView
    private lateinit var fm: FragmentManager
    private var isBack = false
    private val stack = LinkedList<Int>()
    private val profileFragment: ProfileFragment = ProfileFragment.newInstance()
    private val messageFragment: MessagesFragment = MessagesFragment.newInstance()
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
        initViews()
        fm = supportFragmentManager
        mapFragment = fm.fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), MapsFragment::class.java.name) as MapsFragment
        mapFragment.mActivity = this
        bnv.selectedItemId = R.id.map
    }

    private fun initViews(){
        bnv = findViewById(R.id.bottom_menu)
        bnv.setOnNavigationItemSelectedListener {
            if (!isBack) stack.push(it.itemId)
            isBack = false
            Log.d(TAG, "initViews: $stack")
            when (it.itemId){
                R.id.map -> {
                    goToMap()
                    true
                }
                R.id.profile -> {
                    goToProfile()
                    true
                }
                R.id.chat -> {
                    goToMessages()
                    true
                }
                else -> false
            }
        }
    }

    override fun goToProfile() {
        fm.beginTransaction().replace(R.id.main_pane, profileFragment).commit()
    }

    override fun goToMessages() {
        fm.beginTransaction().replace(R.id.main_pane, messageFragment).commit()
    }

    override fun goToMap() {
        fm.beginTransaction().replace(R.id.main_pane, mapFragment).commit()
    }

    override fun onBackPressed() {
        stack.pop()
        if (stack.size >= 1 && bnv.selectedItemId!=R.id.map){
            isBack = true
            bnv.selectedItemId = stack.peek()
        }
        else super.onBackPressed()
    }
    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
}