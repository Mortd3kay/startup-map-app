package com.skyletto.startappfrontend.ui.project.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.ProjectRoleItem
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.HashSet
import java.util.concurrent.TimeUnit

class CreateProjectViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {

    val project : ObservableField<Project> = ObservableField(Project("","",null,null))
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val user = db.userDao().getById(userId)
    val tags = MutableLiveData<MutableSet<Tag>>()
    val chosenTags = MutableLiveData<MutableSet<Tag>>(HashSet())
    private val cd = CompositeDisposable()
    val roles = db.roleDao().getAll()
    init {
        loadRoles()
        loadUser()
        user.observeForever {
            project.get()?.user = it?.user
        }
        loadRandomTags()
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

    fun loadRandomTags() {
        val d = api.apiService.getRandomTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe({ tags: Set<Tag> -> setTags(tags) }
                ) { throwable: Throwable? -> Log.e(TAG, "loadRandomTags: ", throwable) }
        cd.add(d)
    }

    fun loadSimilarTags(str: String?) {
        val d = str?.let {
            api.apiService.getSimilarTags(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry()
                    .subscribe({ tags: Set<Tag> -> setTags(tags) }
                    ) { throwable: Throwable? -> Log.e(TAG, "loadSimilarTags: ", throwable) }
        }
        d?.let { cd.add(it) }
    }

    fun toTagFromChosenTag(tag: Tag) {
        var buffer = tags.value
        buffer?.add(tag)
        tags.value = buffer
        buffer = chosenTags.value
        buffer?.remove(tag)
        chosenTags.value = buffer
    }

    fun toChosenTagFromTag(tag: Tag) {
        var buffer = chosenTags.value
        buffer?.add(tag)
        chosenTags.value = buffer
        buffer = tags.value
        buffer?.remove(tag)
        tags.value = buffer
    }

    private fun setTags(tags: Set<Tag>) {
        val buffer = tags.filter { !chosenTags.value!!.contains(it) }.toMutableSet()
        this.tags.value = buffer
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

    fun prepareProject(roles:List<ProjectRoleItem>){
        packRoles(roles)
        packTags()

    }

    private fun packRoles(roles:List<ProjectRoleItem>){
        project.get()?.roles = roles.map { ProjectAndRole(it.project,it.role.get(),null,it.isSalary.get()!!,it.salaryType.get()!!, it.salaryAmount.get()?.toDouble()!!) }
    }

    private fun packTags(){
        project.get()?.tags = tags.value
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