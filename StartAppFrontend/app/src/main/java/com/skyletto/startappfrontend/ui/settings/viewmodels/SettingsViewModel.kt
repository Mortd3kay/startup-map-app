package com.skyletto.startappfrontend.ui.settings.viewmodels

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.common.utils.isEmailValid
import com.skyletto.startappfrontend.common.utils.isNameValid
import com.skyletto.startappfrontend.common.utils.isPasswordValid
import com.skyletto.startappfrontend.common.utils.toast
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.requests.EditProfileDataRequest
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SettingsViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {
    val handler = Handler(Looper.getMainLooper())
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

    fun sendNewProfile(){
        profile.get()?.let { outerIt ->
            Log.d(TAG, "sendNewProfile: $outerIt")
            api.apiService.editProfile(ApiRepository.makeToken(sp.getString("token", "")!!), outerIt)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                if (it==null) handler.post{ toast(getApplication(), getApplication<MainApplication>().getString(R.string.wrong_password)) }
                                else {
                                    sp.edit().putString("token", it.token).apply()
                                    db.userDao().add(it.user)
                                    it.user.tags?.let { it1 ->
                                        db.tagDao().addAll(it1)
                                        val uTags: List<UserTags> = it1.map { innerIt -> it.user.id?.let { it2 -> UserTags(it2, innerIt.id) }!! }
                                        Log.d(TAG, "loadFromNetwork: $uTags")
                                        db.userTagsDao().addAll(uTags)
                                    }
                                }
                            },
                            {
                                handler.post{ toast(getApplication(), getApplication<MainApplication>().getString(R.string.error_while_editing_profile))}
                                Log.e(TAG, "sendNewProfile: ",it )
                            }
                    )
        }
    }

    private fun loadFromNetwork() {
        val d = api.apiService.getUserByToken(ApiRepository.makeToken(sp.getString("token", "")!!))
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

    fun areFieldsValid():Boolean{
        with(profile.get()){
            this?.let {
                return (isNameValid(firstName) && isNameValid(secondName) && isEmailValid(email) && (isPasswordValid(password) || password.isEmpty()))
            }
            return false
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