package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.*
import com.skyletto.startappfrontend.common.utils.convertLatLngToString
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.data.requests.LatLngRequest
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.User
import com.skyletto.startappfrontend.domain.entities.Location
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Predicate
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class MapViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val projects = db.projectDao().getAll()
    private val myProjects = db.projectDao().getAllByUserId(userId)
    private val users = db.userDao().getAll()
    var isRecommendationShown = false
    var searchField = ObservableField("")
    var userLocations = MutableLiveData<MutableSet<Location>>(HashSet())
    var projectLocations = MutableLiveData<MutableSet<Location>>(HashSet())
    var creationAvailable = true
    private val cd = CompositeDisposable()
    private var userDisposable: Disposable? = null
    private var projectDisposable: Disposable? = null
    var onConditionUpdateListener: OnConditionUpdateListener? = null
    var predicates = arrayOfNulls<Predicate<AlertModel>>(2)
    var recommendationCallback: RecommendationCallback? = null
    var categoryId = MutableLiveData(0L)

    var activity: ActivityFragmentWorker? = null

    init {
        loadUserProjects()
        projects.observeForever { it1 ->
            projectLocations.value?.clear()
            projectLocations.postValue(it1.map {
                it.project.let { it2 -> Location(it2.id, it2.lat, it2.lng, true) }
            }.toMutableSet())
        }
        myProjects.observeForever {
            creationAvailable = it.isEmpty()
        }
        users.observeForever {}
        categoryId.observeForever {
            predicates[0] = when (it) {
                0L -> allPredicate
                1L -> userPredicate
                2L -> projectPredicate
                else -> allPredicate
            }
            Log.d(TAG, "category id: $it")
        }
        setStringCondition()
    }

    private fun loadRecommendationsForUser(latLng: LatLngRequest) {
        val d = api.apiService.getRecommendationsForUser(makeToken(getToken()), latLng)
                .subscribeOn(Schedulers.io())
                .retry()
                .map {
                    saveProjects(it)
                    it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Log.d(TAG, "loadRecommendationsForUser: got projects $it")
                            recommendationCallback?.callback(it.map { p ->
                                var str = p.roles?.joinToString { iit ->
                                    if (iit.user == null) {
                                        return@joinToString iit.role?.name!!
                                    }
                                    return@joinToString ""
                                }?.replace(" ,", "")?.trim(' ',',')?.toLowerCase(Locale.ROOT)
                                if (!str.isNullOrBlank()){
                                    str = if (str.length > 15) getApplication<MainApplication>().getString(R.string.needed) + "..."
                                    else getApplication<MainApplication>().getString(R.string.needed) + str
                                }

                                RecommendationItem(
                                        p.id,
                                        p.user?.id ?: 0,
                                        p.title,
                                        p.user?.firstName + " " + p.user?.secondName?.subSequence(0, 1) + ".",
                                        str,
                                        true
                                )
                            })
                        },
                        {
                            Log.e(TAG, "loadRecommendationsForUser: error", it)
                        })
        cd.add(d)
    }

    private fun loadRecommendationsForProject(project: Project){
        val d = api.apiService.getRecommendationsForProject(makeToken(getToken()), project)
                .subscribeOn(Schedulers.io())
                .retry()
                .map {
                    saveAllUsers(it)
                    it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Log.d(TAG, "loadRecommendationsForUser: got users $it")
                            recommendationCallback?.callback(it.map { u->
                                val str = if (!u.experience.isNullOrBlank()) {
                                    getApplication<MainApplication>().getString(R.string.exp_of_work) + u.experience?.toInt()?.let { it1 -> getApplication<MainApplication>().resources.getQuantityString(R.plurals.years, it1, it1) }
                                } else getApplication<MainApplication>().getString(R.string.exp_of_work) + "нет"
                                RecommendationItem(
                                        u.id?:0,
                                        u.id?:0,
                                        u.firstName+" "+u.secondName.subSequence(0,1)+".",
                                        u.title?:"",
                                        str,
                                        false
                                )
                            })
                        },
                        {
                            Log.e(TAG, "loadRecommendationsForProject: error", it)
                        }
                )
        cd.add(d)
    }

    fun loadRecommendations(latLng: LatLngRequest){
        if (!isRecommendationShown){
            if (myProjects.value!=null && myProjects.value?.isNotEmpty() == true)
                myProjects.value?.get(0)?.let {
                    loadRecommendationsForProject(it.project)
                }
            else loadRecommendationsForUser(latLng)
            isRecommendationShown = true
        }
    }

    fun goToSettings() {
        activity?.goToSettings()
    }

    fun goToCreateProject() {
        activity?.goToCreateProject()
    }

    private fun loadUserProjects() {
        val d = api.apiService.getAllProjects(makeToken(getToken()))
                .delaySubscription(5, TimeUnit.SECONDS)
                .retry()
                .repeat()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            saveProjects(it)
                        },
                        {
                            Log.e(TAG, "loadProjects: error ", it)
                        })

        cd.add(d)
    }

    fun loadProjectLocations(latLng: LatLng, zoom: Float) {
        projectDisposable?.dispose()
        projectDisposable = api.apiService.getClosestProjects(makeToken(getToken()), LatLngRequest(latLng.latitude, latLng.longitude, zoom)).toObservable()
                .subscribeOn(Schedulers.io())
                .retry()
                .repeatWhen { completed -> completed.delay(15, TimeUnit.SECONDS) }
                .subscribe(
                        { oit ->
                            saveProjects(oit)
                        },
                        {
                            Log.e(TAG, "loadLocations: error", it)
                        }
                )
    }

    fun loadLocations(latLng: LatLng, zoom: Float) {
        userDisposable?.dispose()
        userDisposable = api.apiService.getUserLocations(makeToken(getToken()), LatLngRequest(latLng.latitude, latLng.longitude, zoom)).toObservable()
                .subscribeOn(Schedulers.io())
                .retry()
                .repeatWhen { completed -> completed.delay(10, TimeUnit.SECONDS) }
                .subscribe(
                        { oit ->
                            userLocations.value?.clear()
                            userLocations.postValue(oit.filter { it.userId != userId }.toMutableSet())
                            loadUserByIds(oit.map { it.userId })
                        },
                        {
                            Log.e(TAG, "loadLocations: error", it)
                        }
                )
    }

    private fun loadUserByIds(ids: List<Long>) {
        val d = api.apiService.getUsersByIds(makeToken(getToken()), ids.toSet())
                .subscribeOn(Schedulers.io())
                .retry()
                .subscribe(
                        {
                            saveAllUsers(it)
                            Log.d(TAG, "loadUserByIds: userList size = ${it.size}")
                        },
                        {
                            Log.e(TAG, "loadUserByIds: error", it)
                        }
                )
        cd.add(d)
    }

    private fun saveAllUsers(it: List<User>) {
        val tagSet = HashSet<Tag>()
        val uTags = ArrayList<UserTags>()
        for (u in it) {
            u.tags?.let { it1 ->
                tagSet.addAll(it1)
                uTags.addAll(it1.map { it2 -> UserTags(u.id!!, it2.id) })
            }
        }
        db.userDao().addAll(it)
        db.tagDao().addAll(tagSet)
        db.userTagsDao().addAll(uTags)
    }

    private fun saveProjects(it: List<Project>): List<Long> {
        configureProjectsAddresses(it)
        val pIds = db.projectDao().addAll(it)
        for (p in it) {
            p.tags?.let { it1 ->
                db.tagDao().addAll(it1)
                db.projectTagsDao().addAll(it1.map { it2 -> ProjectTags(p.id, it2.id) })
            }
            p.user?.let { it1 ->
                saveUser(it1)
                it1.id?.let { it2 -> db.projectUserDao().add(ProjectUser(p.id, it2)) }
            }
            p.roles?.let { it1 ->
                db.projectAndRolesDao().addAllRoles(it1)
                db.projectRolesDao().addAll(it1.map { it2 -> ProjectRoles(p.id, it2.id) })
            }
        }
        return pIds
    }

    private fun configureProjectsAddresses(projects: List<Project>) {
        for (p in projects) {
            if (p.lat != 0.0 && p.lng != 0.0) p.address = convertLatLngToString(getApplication(), LatLng(p.lat, p.lng))
        }
    }

    private fun saveUser(it1: User) {
        db.userDao().add(it1)
        it1.tags?.let { it2 ->
            db.tagDao().addAll(it2)
            val uTags: List<UserTags> = it2.map { innerIt -> it1.id?.let { it2 -> UserTags(it2, innerIt.id) }!! }
            db.userTagsDao().addAll(uTags)
        }
    }

    @Synchronized
    fun updateMarkers() {
        onConditionUpdateListener?.update(predicates)
    }

    private fun getToken() = getApplication<MainApplication>().getSharedPreferences("profile", Activity.MODE_PRIVATE).getString("token", "")!!

    private fun setStringCondition() {
        predicates[1] = Predicate<AlertModel> { alert ->
            if (alert.isProject) {
                projects.value?.let { oit ->
                    return@Predicate (oit.any { it.project.id == alert.id && projectContainsString(it) })
                }
            } else {
                users.value?.let { oit ->
                    return@Predicate (oit.any { it.user.id == alert.id && userContainsString(it) })
                }
            }
            return@Predicate false
        }
    }

    private fun projectContainsString(it: ProjectWithTagsAndRoles): Boolean {
        val str = searchField.get()!!
        return it.project.title.startsWith(str) || it.tags?.map { it.name }?.any { it.startsWith(str) } ?: false || it.roles?.map { it.role?.name }?.any { it?.startsWith(str) == true } ?: false
    }

    private fun userContainsString(it: UserWithTags): Boolean {
        val str = searchField.get()!!
        return it.user.secondName.startsWith(str) || it.user.firstName.startsWith(str) || it.user.title?.startsWith(str) == true || it.tags?.map { it.name }?.any { it.startsWith(str) } == true
    }

    override fun onCleared() {
        super.onCleared()
        projectDisposable?.dispose()
        userDisposable?.dispose()
        cd.clear()
    }

    companion object {
        private const val TAG = "MAP_VIEW_MODEL"
        private val allPredicate = Predicate<AlertModel> {
            return@Predicate true
        }
        private val userPredicate = Predicate<AlertModel> {
            return@Predicate !it.isProject
        }
        private val projectPredicate = Predicate<AlertModel> {
            return@Predicate it.isProject
        }
    }
}