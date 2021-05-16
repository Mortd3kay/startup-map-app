package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MapViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
//    private val projects = db.projectDao().getAllByUserId(userId)
//    private val cd = CompositeDisposable()

    var search = ""
    var categoryId = 0L

    var activity: ActivityFragmentWorker? = null

    fun goToSettings() {
        activity?.goToSettings()
    }

    fun goToCreateProject() {
        activity?.goToCreateProject()
    }

//    private fun loadProjects() {
//        val d = api.apiService.getAllProjects(makeToken(getToken()))
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                        {
//                            val pIds = db.projectDao().addAll(it)
//
//                        },
//                        {
//
//                        })
//
//        cd.add(d)
//    }

    private fun getToken() = getApplication<MainApplication>().getSharedPreferences("profile", Activity.MODE_PRIVATE).getString("token", "")!!


    companion object {
        private const val TAG = "MAP_VIEW_MODEL"
    }
}