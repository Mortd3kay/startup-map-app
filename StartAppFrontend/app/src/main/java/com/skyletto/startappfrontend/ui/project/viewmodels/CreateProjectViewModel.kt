package com.skyletto.startappfrontend.ui.project.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.ProjectRoleItem
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CreateProjectViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {

    val project : ObservableField<Project> = ObservableField(Project("","",null,null))
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val user = db.userDao().getById(userId)
    private val cd = CompositeDisposable()
    val roles = db.roleDao().getAll()
    init {
        loadRoles()
        loadUser()
        user.observeForever {
            project.get()?.user = it?.user
        }
    }

    private fun loadRoles() {
        val d = api.apiService.getAllRoles(makeToken(getToken()))
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            db.roleDao().addAll(it)
                            Log.d(TAG, "loading roles: $it")
                        },
                        {
                            Log.e(TAG, "loading roles: error", it)
                        }
                )
        cd.add(d)
    }

    private fun loadUser(){
        val d2 = api.apiService.getUserByToken(makeToken(getToken()))
                .delaySubscription(2, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            Log.d(TAG, "loading user: $it")
                            db.userDao().add(it)
                            it.tags?.let { it1 ->
                                db.tagDao().addAll(it1)
                                val uTags: List<UserTags> = it1.map { innerIt -> it.id?.let { it2 -> UserTags(it2, innerIt.id) }!! }
                                Log.d(TAG, "loading user tags: $uTags")
                                db.userTagsDao().addAll(uTags)
                            }
                        },
                        {
                            Log.e(TAG, "loading user: error", it)
                        })
        cd.add(d2)
    }

    fun packRoles(roles:List<ProjectRoleItem>){
        project.get()?.roles = roles.map { ProjectAndRole(it.project,it.role.get(),null,it.isSalary.get()!!,it.salaryType.get()!!, it.salaryAmount.get()?.toDouble()!!) }
    }

    private fun getToken() = getApplication<MainApplication>().getSharedPreferences("profile", Activity.MODE_PRIVATE).getString("token", "")!!


    companion object{
        private const val TAG = "CREATE_PROJECT_VIEW_MODEL"
        const val ROLES_MAX_COUNT = 5
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }
}