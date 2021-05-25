package com.skyletto.startappfrontend.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.data.database.AppDatabase
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.domain.entities.User
import com.skyletto.startappfrontend.ui.main.fragments.MapsFragment
import com.skyletto.startappfrontend.ui.main.fragments.MessagesFragment
import com.skyletto.startappfrontend.ui.main.fragments.ProfileFragment
import com.skyletto.startappfrontend.ui.main.viewmodels.ProfileViewModel
import com.skyletto.startappfrontend.ui.project.ProjectActivity
import com.skyletto.startappfrontend.ui.settings.SettingsActivity
import com.skyletto.startappfrontend.ui.start.StartActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity(), ActivityFragmentWorker {
    private lateinit var bnv: BottomNavigationView
    private lateinit var fm: FragmentManager
    private lateinit var api:ApiRepository
    private lateinit var db:AppDatabase

    private var isBack = false
    private val stack = LinkedList<Int>()
    private val cd = CompositeDisposable()

    private lateinit var profileFragment: ProfileFragment
    private lateinit var messageFragment :MessagesFragment
    private lateinit var mapFragment: MapsFragment

    private var token : String? = null
    private var id : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        api = (application as MainApplication).api
        db = (application as MainApplication).db
        parseBundle()
        loadUser()
        initViews()
        initFragments()
        bnv.selectedItemId = R.id.map
    }

    private fun initFragments(){
        fm = supportFragmentManager
        mapFragment = fm.fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), MapsFragment::class.java.name) as MapsFragment
        profileFragment = id?.let { ProfileFragment.newInstance(it) }!!
        messageFragment = MessagesFragment.newInstance()
        mapFragment.mActivity = this
        messageFragment.mActivity = this
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

    private fun parseBundle(){
        val bundle = intent.extras
        token = bundle?.getString("token")
        id = bundle?.getLong("id")
        Log.d(TAG, "parseBundle: $id")
        if (token == null) {
            val sp = getSharedPreferences("profile", MODE_PRIVATE)
            token = sp.getString("token", "")
            if (token == "") {
                startActivity(Intent(this@MainActivity, StartActivity::class.java))
                finish()
            }
            id = sp.getLong("id", -1)
        }
    }

    private fun loadUser(){
        token?.let {
            id?.let { it2 ->
                val d = api.apiService.getUserById(makeToken(it), it2)
                        .subscribeOn(Schedulers.io())
                        .retry()
                        .subscribe(
                                {
                                    saveUser(it)
                                },
                                {
                                    Log.e(TAG, "loadUser: error", it)
                                }
                        )
                cd.add(d)
            }
        }

    }

    private fun saveUser(it1: User){
        db.userDao().add(it1)
        it1.tags?.let { it2 ->
            db.tagDao().addAll(it2)
            val uTags: List<UserTags> = it2.map { innerIt -> it1.id?.let { it2 -> UserTags(it2, innerIt.id) }!! }
            Log.d(TAG, "saveUser: $uTags")
            db.userTagsDao().addAll(uTags)
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

    override fun goToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun goToCreateProject() {
        startActivity(Intent(this, ProjectActivity::class.java))
    }

    override fun getToken():String {
        return token?:""
    }

    override fun getUserId():Long {
        return id?:-1
    }

    override fun onBackPressed() {
        stack.pop()
        if (stack.size >= 1 && bnv.selectedItemId!=R.id.map){
            isBack = true
            bnv.selectedItemId = stack.peek()
        }
        else super.onBackPressed()
    }

    override fun onDestroy() {
        cd.clear()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
}