package com.skyletto.startappfrontend.common.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.ui.chat.viewmodels.ChatViewModel
import com.skyletto.startappfrontend.ui.main.viewmodels.ProfileViewModel
import com.skyletto.startappfrontend.ui.settings.viewmodels.SettingsViewModel


class ProfileViewModelFactory(private val mApplication: Application, private val chatId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(mApplication, chatId) as T
    }
}