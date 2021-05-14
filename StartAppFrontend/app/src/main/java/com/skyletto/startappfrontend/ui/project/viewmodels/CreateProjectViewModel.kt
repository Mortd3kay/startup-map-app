package com.skyletto.startappfrontend.ui.project.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.ProjectRole

class CreateProjectViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {

    val project : ObservableField<Project> = ObservableField(Project("","",null,null))
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val user = db.userDao().getById(userId)
    val roles = db.roleDao().getAll()
    init {
        user.observeForever {
            project.get()?.user = it?.user
        }
    }


    companion object{
        private const val TAG = "CREATE_PROJECT_VIEW_MODEL"
        const val ROLES_MAX_COUNT = 5
    }
}