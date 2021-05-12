package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.domain.entities.User

class ProfileViewModel(application: Application, val id:Long) : AndroidViewModel(application){
    val api = getApplication<MainApplication>().api
    val db = getApplication<MainApplication>().db
    val user = db.userDao().getById(id)
    val vUser = ObservableField<UserWithTags>()
    init {
        user.observeForever {
            vUser.set(it)
        }
    }
}