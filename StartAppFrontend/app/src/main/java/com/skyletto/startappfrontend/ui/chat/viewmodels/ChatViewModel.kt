package com.skyletto.startappfrontend.ui.chat.viewmodels

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.common.utils.toast
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.domain.entities.Message
import com.skyletto.startappfrontend.domain.entities.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class ChatViewModel(application: Application, val chatId: Long) : AndroidViewModel(application) {
    var messageText = ""
    var friend: ObservableField<User> = ObservableField()
    private val api = getApplication<MainApplication>().api
    private val db = getApplication<MainApplication>().db
    private val cd = CompositeDisposable()
    private val sp = application.getSharedPreferences("profile", AppCompatActivity.MODE_PRIVATE)
    private val ownerId = sp.getLong("id", -1)
    var messages: LiveData<List<Message>> = db.messageDao().getAllByChatId(chatId)

    fun sendMessage() {
        val message = Message(messageText, LocalDateTime.now().toString(), ownerId, chatId)
        Log.d(TAG, "sendMessage: $message")
        val d = api.apiService.addMessage(ApiRepository.makeToken(sp.getString("token", "")!!), message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it == null) toast(getApplication(), getApplication<MainApplication>().getString(R.string.cant_send_message))
                        },
                        {
                            Log.e(TAG, "sendMessage: ", it)
                            toast(getApplication(), getApplication<MainApplication>().getString(R.string.error_while_sending))
                        }
                )
        cd.add(d)
        messageText = ""
    }

    init {
        var d = loadFriend(chatId)
                .delaySubscription(10, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            friend.set(it)
                        },
                        {
                            Log.e(TAG, "loadFriend: error", it)
                        }
                )
        cd.add(d)

        d = loadMessages(chatId)
                .delaySubscription(1, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            db.messageDao().addAll(it)
                        },
                        {
                            Log.e(TAG, "loadMessages: error", it)
                        }
                )
        cd.add(d)

    }

    private fun loadFriend(id: Long) = api.apiService.getUserById(ApiRepository.makeToken(sp.getString("token", "")!!), id)

    private fun loadMessages(id: Long) = api.apiService.getMessagesFromChat(ApiRepository.makeToken(sp.getString("token", "")!!), id)

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    companion object {
        private const val TAG = "CHAT_VIEW_MODEL"
    }
}