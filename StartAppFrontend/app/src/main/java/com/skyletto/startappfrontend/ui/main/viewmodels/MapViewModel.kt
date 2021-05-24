package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.ProjectRoles
import com.skyletto.startappfrontend.common.models.ProjectTags
import com.skyletto.startappfrontend.common.models.ProjectUser
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.common.utils.convertLatLngToString
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.data.requests.LatLngRequest
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.User
import com.skyletto.startappfrontend.domain.entities.Location
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MapViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val projects = db.projectDao().getAllByUserId(userId)
    var locations = MutableLiveData<MutableSet<Location>>(HashSet())
    var creationAvailable = true
    private val cd = CompositeDisposable()

    init {
        loadProjects()
        projects.observeForever { it1 ->
            creationAvailable = it1.isEmpty()
            addLocations(it1.map {
                it.project.let { it2->
                    Location(it2.id, it2.lat, it2.lng, true)
                }
            })
        }
    }

    var search = ""
    var categoryId = 0L

    var activity: ActivityFragmentWorker? = null

    fun goToSettings() {
        activity?.goToSettings()
    }

    fun goToCreateProject() {
        activity?.goToCreateProject()
    }

    private fun loadProjects() {
        val d = api.apiService.getAllProjects(makeToken(getToken()))
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

    fun loadLocations(latLng: LatLng, zoom:Int){
        val d = api.apiService.getLocations(makeToken(getToken()), LatLngRequest(latLng.latitude, latLng.longitude, zoom))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            addLocations(it)
                        },
                        {
                            Log.e(TAG, "loadLocations: error", it)
                        }
                )
    }

    private fun addLocations(it:Iterable<Location>){
        val buf = locations.value
        buf?.addAll(it)
        locations.postValue(buf)
    }

    private fun saveProjects(it:List<Project>) : List<Long>{
        configureProjectsAddresses(it)
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

    private fun configureProjectsAddresses(projects:List<Project>){
        for (p in projects){
            if (p.lat!=null && p.lng!=null) p.address = convertLatLngToString(getApplication(), LatLng(p.lat!!, p.lng!!))
        }
    }

    private fun saveUser(it1: User){
        db.userDao().add(it1)
        it1.tags?.let { it2 ->
            db.tagDao().addAll(it2)
            val uTags: List<UserTags> = it2.map { innerIt -> it1.id?.let { it2 -> UserTags(it2, innerIt.id) }!! }
            db.userTagsDao().addAll(uTags)
        }
    }

    private fun getToken() = getApplication<MainApplication>().getSharedPreferences("profile", Activity.MODE_PRIVATE).getString("token", "")!!


    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    companion object {
        private const val TAG = "MAP_VIEW_MODEL"
    }
}