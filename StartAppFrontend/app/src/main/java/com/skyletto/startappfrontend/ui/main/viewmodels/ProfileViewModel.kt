package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.*
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.User
import com.skyletto.startappfrontend.ui.project.viewmodels.CreateProjectViewModel
import com.skyletto.startappfrontend.ui.settings.viewmodels.SettingsViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ProfileViewModel(application: Application, val id:Long) : AndroidViewModel(application){
    val api = getApplication<MainApplication>().api
    val db = getApplication<MainApplication>().db
    private val sp = getApplication<MainApplication>().getSharedPreferences("profile", Application.MODE_PRIVATE)
    private val cd = CompositeDisposable()
    val user = db.userDao().getById(id)
    val projects = db.projectDao().getAllByUserId(id)
    val roles = db.roleDao().getAll()
    val vUser = ObservableField<UserWithTags>()
    init {
        loadFromNetwork()
        loadProjects()
        loadRoles()
        user.observeForever {
            vUser.set(it)
        }
    }

    private fun loadFromNetwork() {
        val d = api.apiService.getUserByToken(ApiRepository.makeToken(sp.getString("token", "")!!))
                .delaySubscription(2, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            Log.d(TAG, "loadFromNetwork user: $it")
                            saveUser(it)
                        },
                        {
                            Log.e(TAG, "loadFromNetwork: ", it)
                        })
        cd.add(d)
    }

    private fun loadProjects() {
        val d = api.apiService.getAllProjects(ApiRepository.makeToken(getToken()))
                .delaySubscription(5, TimeUnit.SECONDS)
                .retry()
                .repeat()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            val p = saveProjects(it)
                            Log.d(TAG, "loadProjects: $p")
                        },
                        {
                            Log.e(TAG, "loadProjects: error ", it)
                        })

        cd.add(d)
    }

    private fun saveProjects(it:List<Project>) : List<Long>{
        val pIds = db.projectDao().addAll(it)
        for (p in it){
            p.tags?.let { it1 ->
                db.tagDao().addAll(it1)
                db.projectTagsDao().addAll(it1.map { it2 -> ProjectTags(p.id, it2.id) })
            }
            p.user?.let { it1 ->
                saveUser(it1)
                it1.id?.let { it2 -> db.projectUserDao().add(ProjectUser(p.id,it2)) }
            }
            p.roles?.let { it1 ->
                db.projectAndRolesDao().addAllRoles(it1)
                db.projectRolesDao().addAll(it1.map { it2 -> ProjectRoles(p.id, it2.id) })
            }
        }
        return pIds
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

    fun deleteProject(project: Project) {
        val d = api.apiService.removeProject(makeToken(getToken()), project)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            db.projectDao().removeProject(project)
                            Log.d(TAG, "deleteProject: $it")
                        },
                        {
                            Log.e(TAG, "deleteProject: error", it)
                        }
                )
        cd.add(d)
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

    private fun getToken() = getApplication<MainApplication>().getSharedPreferences("profile", Activity.MODE_PRIVATE).getString("token", "")!!


    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    companion object{
        private const val TAG = "PROFILE_VIEW_MODEL"
    }
}