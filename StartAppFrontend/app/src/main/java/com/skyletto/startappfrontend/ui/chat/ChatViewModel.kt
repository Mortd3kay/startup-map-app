package com.skyletto.startappfrontend.ui.chat

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.domain.entities.Message
import com.skyletto.startappfrontend.domain.entities.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    var chatId: Long? = null
    set(value) {
        field = value
        loadDialog()
    }
    var friend: ObservableField<User> = ObservableField()
    private val api = getApplication<MainApplication>().api
    private val cd = CompositeDisposable()
    private val sp = application.getSharedPreferences("profile", AppCompatActivity.MODE_PRIVATE)
    private val ownerId = sp.getLong("id", -1)
    var messages: MutableLiveData<List<Message>> = MutableLiveData()

    private fun loadDialog(){
        var d = loadFriend(chatId!!)
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
                            Log.e(TAG, "loadFriend: ", it)
                        }
                )
        cd.add(d)

        d = loadMessages(chatId!!)
                .delaySubscription(1, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            messages.postValue(it)
                        },
                        {
                            Log.e(TAG, "loadMessages: ", it)
                        }
                )
        cd.add(d)
    }

    private fun loadFriend(id:Long) = api.apiService.getUserById(ApiRepository.makeToken(sp.getString("token", "")!!), id)

    private fun loadMessages(id:Long) = api.apiService.getMessagesFromChat(ApiRepository.makeToken(sp.getString("token", "")!!), id)

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    companion object{
        private const val TAG = "CHAT_VIEW_MODEL"
    }
}