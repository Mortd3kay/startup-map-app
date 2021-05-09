package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.domain.entities.Chat
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MessagesViewModel(application: Application) : AndroidViewModel(application) {
    private var id: Long? = null
    var activity: ActivityFragmentWorker? = null
        set(value) {
            field = value
            id = field?.getUserId()
        }
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val cd = CompositeDisposable()
    private val sp = application.getSharedPreferences("profile", AppCompatActivity.MODE_PRIVATE)
    var chats = db.chatDao().getAll()


    fun loadChats() {
        val a = activity?.let { getChats(it) }
        val d = a?.flatMap(
                {
                    val set = mutableSetOf<Long>()
                    for (m in it) {
                        val friendId = if (id == m.senderId) m.receiverId else m.senderId
                        set.add(friendId)
                    }
                    getUsers(set)
                },
                { messages, users ->
                    val list = mutableListOf<Chat>()
                    for (m in messages) {
                        val user = users.first { it.id == m.senderId || it.id == m.receiverId }
                        list.add(Chat(message = m, chatId = user.id!!, chatName = "${user.firstName} ${user.secondName}"))
                    }
                    list
                })
                ?.repeatWhen{completed -> completed.delay(3, TimeUnit.SECONDS)}
                ?.subscribeOn(Schedulers.io())
                ?.retry()
                ?.subscribe(
                        {
                            db.chatDao().addAll(it)
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