package com.skyletto.startappfrontend.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.adapters.MessagesAdapter
import com.skyletto.startappfrontend.common.models.MessageItem
import com.skyletto.startappfrontend.databinding.ActivityChatBinding
import com.skyletto.startappfrontend.ui.chat.viewmodels.ChatViewModel
import com.skyletto.startappfrontend.common.utils.ChatViewModelFactory
import com.skyletto.startappfrontend.ui.chat.viewmodels.OnDownPositionListener
import com.skyletto.startappfrontend.ui.chat.viewmodels.OnMessageAddedListener
import com.skyletto.startappfrontend.ui.settings.SettingsActivity

class ChatActivity : AppCompatActivity() {
    private var viewModel: ChatViewModel? = null
    private lateinit var binding: ActivityChatBinding
    private val adapter = MessagesAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatId = intent.extras?.getLong("id")?:-1
        configureViewModel(chatId)
        configureAdapter()
        initViews()
    }

    private fun configureViewModel(chatId: Long){
        viewModel = ViewModelProvider(this, ChatViewModelFactory(application, chatId)).get(ChatViewModel::class.java)
        viewModel?.messages?.observe(this){outerIt->
            val items = outerIt.map { MessageItem(it.text, it.time, it.isChecked, it.senderId, it.receiverId, chatId==it.receiverId) }
            adapter.messages = items
        }
    }

    private fun configureAdapter(){
        adapter.drop = viewModel?.forceDrop!!
        adapter.onDownPositionListener = object : OnDownPositionListener {
            override fun check(isDown:Boolean) {
                if (isDown) {
                    binding.chatScrollBtn.visibility = View.GONE

                } else {
                    binding.chatScrollBtn.visibility = View.VISIBLE
                }
            }
        }
        adapter.onMessageAddedListener = object : OnMessageAddedListener {
            override fun update() {
                adapter?.itemCount?.let {
                    if (it >1)
                        binding.messagesRv.smoothScrollToPosition(it - 1)
                }

            }

        }
    }

    private fun initViews(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        binding.chatTbSettings.setOnClickListener { startActivity(Intent(this@ChatActivity, SettingsActivity::class.java)) }
        binding.chatBackBtn.setOnClickListener { onBackPressed() }
        val llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        llm.isSmoothScrollbarEnabled = true
        binding.messagesRv.layoutManager = llm
        binding.messagesRv.adapter = adapter
        binding.messagesRv.scrollToPosition(adapter.itemCount-1)
        binding.chatSendBtn.setOnClickListener {
            viewModel?.sendMessage()
        }
        binding.chatScrollBtn.setOnClickListener { binding.messagesRv.smoothScrollToPosition(adapter.itemCount-1) }
        binding.model = viewModel
    }

    companion object{
        private  const val TAG = "CHAT_ACTIVITY"
    }
}