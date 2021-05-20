package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.domain.entities.Chat
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.domain.entities.Message
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.domain.entities.User
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
    var chats = db.chatDao().getAll()


    fun loadChats() {
        activity?.let {
            val a = getChats(it)
            val d = a.flatMap(
                    {
                        val set = mutableSetOf<Long>()
                        for (m in it) {
                            val friendId = if (id == m.senderId) m.receiverId else m.senderId
                            set.add(friendId)
                        }
                        getUsers(set)
                    },
                    { messages, users ->
                        saveAllUsers(users)
                        makeChats(messages, users)
                    })
                    .repeatWhen { completed -> completed.delay(3, TimeUnit.SECONDS) }
                    .subscribeOn(Schedulers.io())
                    .retry()
                    .subscribe(
                            {it2->
                                db.chatDao().addAll(it2)
                            },
                            { it2->
                                Log.e(TAG, "loadChats: error ", it2.fillInStackTrace())
                            })

            cd.add(d)
        }

    }

    private fun makeChats(messages: List<Message>, users: List<User>):List<Chat> {
        val list = mutableListOf<Chat>()
        for (m in messages) {
            val user = users.first { it.id == m.senderId || it.id == m.receiverId }
            list.add(Chat(message = m, chatId = user.id!!, chatName = "${user.firstName} ${user.secondName}"))
        }
        return list
    }

    private fun saveAllUsers(it: List<User>){
        val tagSet = HashSet<Tag>()
        val uTags = ArrayList<UserTags>()
        for (u in it){
            u.tags?.let {
                it1 -> tagSet.addAll(it1)
                uTags.addAll(it1.map { it2 -> UserTags(u.id!!, it2.id) })
            }
        }
        db.userDao().addAll(it)
        db.tagDao().addAll(tagSet)
        db.userTagsDao().addAll(uTags)
    }

    private fun getChats(activity: ActivityFragmentWorker) = api.apiService.getChats(ApiRepository.makeToken(getToken())).toObservable()

    private fun getUsers(ids: Set<Long>) = api.apiService.getUsersByIds(ApiRepository.makeToken(getToken()), ids).toObservable()

    private fun getToken() = getApplication<MainApplication>().getSharedPreferences("profile", Activity.MODE_PRIVATE).getString("token", "")!!

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