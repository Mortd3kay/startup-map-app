package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.common.ChatItem
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MessagesViewModel(application: Application) : AndroidViewModel(application) {
    var activity: ActivityFragmentWorker? = null
    private val api = getApplication<MainApplication>().api
    private val cd = CompositeDisposable()
    var chats: MutableLiveData<MutableList<ChatItem>> = MutableLiveData(mutableListOf())
    private val id = activity?.getUserId()

    fun loadChats(){
        activity?.let {outerIt->
            val d = api.apiService.getChats(ApiRepository.makeToken(outerIt.getToken()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                Log.d(TAG, "loadChats: $it")
                                val list: MutableList<ChatItem> = mutableListOf()
                                for (m in it){
                                    var friendId = if (id == m.senderId) m.receiverId else m.senderId
                                    list.add(ChatItem(message = m, chatId = friendId))
                                }
                                chats.postValue(list)
                            },
                            {
                                Log.e(TAG, "loadChats: error ", it)
                            })
            cd.add(d)
        }

    }

    fun goToSettings() {
        activity?.goToSettings()
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    companion object{
        private const val TAG = "MESSAGES_VIEW_MODEL"
    }
}