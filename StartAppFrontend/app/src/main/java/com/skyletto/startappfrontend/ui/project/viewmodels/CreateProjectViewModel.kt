package com.skyletto.startappfrontend.ui.project.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.domain.entities.Project

class CreateProjectViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {
    val project : ObservableField<Project> = ObservableField(Project())
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val user = db.userDao().getById(userId)
    init {
        user.observeForever {
            project.get()?.user = it.user
        }
    }

    companion object{
        private const val TAG = "CREATE_PROJECT_VIEW_MODEL"
        private const val ROLES_MAX_COUNT = 5
    }
}