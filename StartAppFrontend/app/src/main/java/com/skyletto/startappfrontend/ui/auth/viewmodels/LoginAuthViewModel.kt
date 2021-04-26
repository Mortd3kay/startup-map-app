package com.skyletto.startappfrontend.ui.auth.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.requests.LoginDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginAuthViewModel(application: Application) : AndroidViewModel(application) {
    var data = ObservableField(LoginDataRequest(null, null))
    private var onSuccessLoginListener: OnSuccessLoginListener? = null
    private var onErrorLoginListener: OnErrorLoginListener? = null
    private var onSaveProfileListener: OnSaveProfileListener? = null
    private var goToRegister: GoToRegister? = null
    @SuppressLint("CheckResult")
    fun login() {
        data.get()?.let {
            ApiRepository.apiService.login(it)
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
}