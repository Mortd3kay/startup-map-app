package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.common.ChatItem
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.domain.entities.Message
import com.skyletto.startappfrontend.domain.entities.User
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.function.Function

class MessagesViewModel(application: Application) : AndroidViewModel(application) {
    private var id: Long? = null
    var activity: ActivityFragmentWorker? = null
        set(value) {
            field = value
            id = field?.getUserId()
        }
    private val api = getApplication<MainApplication>().api
    private val cd = CompositeDisposable()
    private val sp = application.getSharedPreferences("profile", AppCompatActivity.MODE_PRIVATE)
    var chats: MutableLiveData<MutableList<ChatItem>> = MutableLiveData(mutableListOf())


    fun loadChats() {
        val a = activity?.let { getChats(it) }
        val d = a?.flatMap(
                {
                    Log.d(TAG, "loadChats: $it")
                    val set = mutableSetOf<Long>()
                    for (m in it) {
                        val friendId = if (id == m.senderId) m.receiverId else m.senderId
                        set.add(friendId)
                    }
                    getUsers(set)
                },
                { messages, users ->
                    val list: MutableList<ChatItem> = mutableListOf()
                    for (m in messages) {
                        val user = users.first { it.id == m.senderId || it.id == m.receiverId }
                        list.add(ChatItem(message = m, chatId = user.id!!, chatName = "${user.firstName} ${user.secondName}"))
                    }
                    list
                })
                ?.subscribeOn(Schedulers.io())
                ?.retry()
                ?.subscribe(
                        {
                            if (chats.value != null){
                                chats.value?.let { innerIt ->
                                    if (innerIt.size < it.size)
                                        chats.postValue(it)
                                }
                            } else chats.postValue(it)

                        },
                        {
                            Log.e(TAG, "loadChats: error ", it.fillInStackTrace())
                        })

        if (d != null) {
            cd.add(d)
        }


    }

    private fun getChats(activity: ActivityFragmentWorker) = api.apiService.getChats(ApiRepository.makeToken(activity.getToken())).toObservable()

    private fun getUsers(ids: Set<Long>) = api.apiService.getUsersByIds(ApiRepository.makeToken(sp.getString("token", "")!!),ids).toObservable()

    fun goToSettings() {
        activity?.goToSettings()
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    companion object {
        private const val TAG = "MESSAGES_VIEW_MODEL"
    }
}