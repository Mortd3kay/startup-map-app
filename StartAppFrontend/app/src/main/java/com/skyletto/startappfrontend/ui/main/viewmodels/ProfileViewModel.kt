package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.domain.entities.User
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
    val vUser = ObservableField<UserWithTags>()
    init {
        loadFromNetwork()
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
                            db.userDao().add(it)
                            it.tags?.let { it1 ->
                                db.tagDao().addAll(it1)
                                val uTags: List<UserTags> = it1.map { innerIt -> it.id?.let { it2 -> UserTags(it2, innerIt.id) }!! }
                                Log.d(TAG, "loadFromNetwork: $uTags")
                                db.userTagsDao().addAll(uTags)
                            }
                        },
                        {
                            Log.e(TAG, "loadFromNetwork: ", it)
                        })
        cd.add(d)
    }

    companion object{
        private const val TAG = "PROFILE_VIEW_MODEL"
    }
}