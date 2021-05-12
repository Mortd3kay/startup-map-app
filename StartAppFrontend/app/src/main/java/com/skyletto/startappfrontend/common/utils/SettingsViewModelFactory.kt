package com.skyletto.startappfrontend.common.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.ui.chat.viewmodels.ChatViewModel
import com.skyletto.startappfrontend.ui.settings.viewmodels.SettingsViewModel


class SettingsViewModelFactory(private val mApplication: Application, private val chatId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(mApplication, chatId) as T
    }
}