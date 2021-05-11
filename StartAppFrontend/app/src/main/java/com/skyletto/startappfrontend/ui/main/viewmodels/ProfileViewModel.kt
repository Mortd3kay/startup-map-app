package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication

class ProfileViewModel(application: Application, val id:Long) : AndroidViewModel(application){
    val api = getApplication<MainApplication>().api
    val db = getApplication<MainApplication>().db
    val user = db.userDao().getById(id)
}