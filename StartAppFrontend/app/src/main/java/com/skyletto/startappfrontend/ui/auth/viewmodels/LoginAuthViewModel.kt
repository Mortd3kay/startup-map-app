package com.skyletto.startappfrontend.ui.auth.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.requests.LoginDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginAuthViewModel(application: Application) : AndroidViewModel(application) {
    var data = ObservableField(LoginDataRequest(null, null))
    private var onSuccessLoginListener: OnSuccessLoginListener? = null
    private var onErrorLoginListener: OnErrorLoginListener? = null
    private var onSaveProfileListener: OnSaveProfileListener? = null
    private var goToRegister: GoToRegister? = null

    @SuppressLint("CheckResult")
    fun login() {
        data.get()?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val response = ApiRepository.apiService.login(it)
                if (response.isSuccessful){
                    val pr = response.body()
                    pr?.let {
                        saveProfileInfo(it)
                        onSuccessLoginListener?.onSuccess(it)
                    }
                } else {
                    Log.e(TAG, "login error response:  ${response.errorBody()!!}")
                    onErrorLoginListener?.onError()
                }
            }
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