package com.skyletto.startappfrontend.common

import android.app.Application
import com.skyletto.startappfrontend.data.database.AppDatabase
import com.skyletto.startappfrontend.data.network.ApiRepository

class MainApplication: Application() {
    lateinit var db: AppDatabase
    val api = ApiRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = AppDatabase.getInstance(applicationContext)
    }

    companion object {
        lateinit var instance: MainApplication
            private set
    }
}