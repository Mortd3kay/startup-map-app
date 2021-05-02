package com.skyletto.startappfrontend.ui.auth.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skyletto.startappfrontend.data.requests.LoginDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.utils.MainApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginAuthViewModel(application: Application) : AndroidViewModel(application) {
    var data = ObservableField(LoginDataRequest(null, null))
    private val api = getApplication<MainApplication>().api
    private var onSuccessLoginListener: OnSuccessLoginListener? = null
    private var onErrorLoginListener: OnErrorLoginListener? = null
    private var onSaveProfileListener: OnSaveProfileListener? = null
    private var goToRegister: GoToRegister? = null

    @SuppressLint("CheckResult")
    fun login() {
        data.get()?.let {
            api.apiService.login(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { profileResponse: ProfileResponse ->
                                saveProfileInfo(profileResponse)
                                onSuccessLoginListener?.onSuccess(profileResponse)
                            }
                    ) { onErrorLoginListener?.onError() }
        }
    }

    fun setOnSuccessLoginListener(onSuccessLoginListener: OnSuccessLoginListener?) {
        this.onSuccessLoginListener = onSuccessLoginListener
    }

    fun setOnErrorLoginListener(onErrorLoginListener: OnErrorLoginListener?) {
        this.onErrorLoginListener = onErrorLoginListener
    }

    fun setOnSaveProfileListener(onSaveProfileListener: OnSaveProfileListener?) {
        this.onSaveProfileListener = onSaveProfileListener
    }

    fun setGoToRegister(goToRegister: GoToRegister?) {
        this.goToRegister = goToRegister
    }

    private fun saveProfileInfo(pr: ProfileResponse) {
        onSaveProfileListener?.save(pr)
    }

    fun goToRegister() {
        goToRegister?.goTo()
    }

    companion object {
        private const val TAG = "LOGIN_VIEW_MODEL"
    }
}