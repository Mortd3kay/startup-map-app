package com.skyletto.startappfrontend.ui.settings.viewmodels

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.requests.EditProfileDataRequest
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SettingsViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {
    val api = getApplication<MainApplication>().api
    val db = getApplication<MainApplication>().db
    private val sp = getApplication<MainApplication>().getSharedPreferences("profile", Application.MODE_PRIVATE)
    private val cd = CompositeDisposable()
    var user = db.userDao().getById(userId)
    var profile: ObservableField<EditProfileDataRequest> = ObservableField()

    init {
        loadFromNetwork()
        observeDB()
    }

    private fun loadFromNetwork() {
        val d = api.apiService.getUserByToken(ApiRepository.makeToken(sp.getString("token", "")!!))
                .delaySubscription(5, TimeUnit.SECONDS)
                .retry()
                .repeat()
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

    private fun observeDB() {
        user.observeForever { outerIt ->
            outerIt?.user?.let {
                profile.set(
                        EditProfileDataRequest(
                                userId,
                                it.email,
                                "",
                                "",
                                it.firstName,
                                it.secondName,
                                it.phoneNumber ?: "",
                                it.title,
                                it.experience,
                                it.description,
                                it.tags
                        )
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    companion object {
        private const val TAG = "SETTINGS_VIEW_MODEL"
    }

}