package com.skyletto.startappfrontend.ui.chat.viewmodels

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.common.utils.toast
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.domain.entities.Message
import com.skyletto.startappfrontend.domain.entities.User
import com.skyletto.startappfrontend.ui.main.viewmodels.ProfileViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class ChatViewModel(application: Application, private val chatId: Long) : AndroidViewModel(application) {
    var forceDrop = true
    private val mObservable = MutableLiveData<Long>(0)
    var messageText = ObservableField("")
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val cd = CompositeDisposable()
    private var disposable: Disposable? = null
    private val sp = application.getSharedPreferences("profile", AppCompatActivity.MODE_PRIVATE)
    private val ownerId = sp.getLong("id", -1)
    private var mFriend = db.userDao().getById(chatId)
    var friend = ObservableField<User>()
    var messages: LiveData<List<Message>> = db.messageDao().getAllByChatId(chatId)

    init {
        loadFriend()
        loadMessages()
        mObservable.observeForever{
            Log.d(TAG, "observable: $it")
            disposable?.dispose()
            loadMessages()
        }

        mFriend.observeForever {
            friend.set(it.user)
        }
    }


    fun sendMessage() {
        val message = Message(messageText.get()!!, LocalDateTime.now().toString(), ownerId, chatId)
        Log.d(TAG, "sendMessage: $message")
        val d = api.apiService.addMessage(ApiRepository.makeToken(sp.getString("token", "")!!), message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it == null) toast(getApplication(), getApplication<MainApplication>().getString(R.string.cant_send_message))
                            else forceDrop = true
                        },
                        {
                            Log.e(TAG, "sendMessage: ", it)
                            toast(getApplication(), getApplication<MainApplication>().getString(R.string.error_while_sending))
                        }
                )
        cd.add(d)
        messageText.set("")
    }

    private fun saveUser(it1: User){
        db.userDao().add(it1)
        it1.tags?.let { it2 ->
            db.tagDao().addAll(it2)
            val uTags: List<UserTags> = it2.map { innerIt -> it1.id?.let { it2 -> UserTags(it2, innerIt.id) }!! }
            Log.d(TAG, "saveUser: $uTags")
            db.userTagsDao().addAll(uTags)
        }
    }

    private fun loadFriend() {
        val d = loadFriend(chatId)
                .delaySubscription(10, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            saveUser(it)
                        },
                        {
                            Log.e(TAG, "loadFriend: error", it)
                        }
                )
        cd.add(d)
    }

    private fun loadFriend(id: Long) = api.apiService.getUserById(ApiRepository.makeToken(sp.getString("token", "")!!), id)

    private fun loadMessages() {
        disposable = loadMessages(chatId, mObservable.value!!)
                .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS) }
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            val saved = db.messageDao().addAll(it)
                            Log.d(TAG, "saved messages: $saved")
                            if (it.isNotEmpty()) {
                                val lastId = it.maxOf { innerIt -> innerIt.id!! }
                                Log.d(TAG, "last id: $lastId")
                                mObservable.postValue(lastId)
                            }
                        },
                        {
                            Log.e(TAG, "loadMessages: error", it)
                        }
                )
    }

    private fun loadMessages(id: Long, lastId: Long): Single<List<Message>> {
        Log.d(TAG, "loadMessages: $lastId")
        return api.apiService.getMessagesFromChat(ApiRepository.makeToken(sp.getString("token", "")!!), id, lastId)
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
        disposable?.dispose()
    }

    companion object {
        private const val TAG = "CHAT_VIEW_MODEL"
    }
}