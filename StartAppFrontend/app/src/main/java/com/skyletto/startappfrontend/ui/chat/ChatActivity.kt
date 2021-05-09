package com.skyletto.startappfrontend.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MessageItem
import com.skyletto.startappfrontend.common.adapters.MessagesAdapter
import com.skyletto.startappfrontend.databinding.ActivityChatBinding
import com.skyletto.startappfrontend.ui.chat.viewmodels.ChatViewModel
import com.skyletto.startappfrontend.ui.chat.viewmodels.ChatViewModelFactory
import com.skyletto.startappfrontend.ui.chat.viewmodels.OnDownPositionListener
import com.skyletto.startappfrontend.ui.chat.viewmodels.OnMessageAddedListener
import com.skyletto.startappfrontend.ui.settings.SettingsActivity

class ChatActivity : AppCompatActivity() {
    private var viewModel: ChatViewModel? = null
    private lateinit var binding: ActivityChatBinding
    private val adapter = MessagesAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        val chatId = intent.extras?.getLong("id")?:-1
        viewModel = ViewModelProvider(this, ChatViewModelFactory(application, chatId)).get(ChatViewModel::class.java)
        binding.model = viewModel
        adapter.drop = viewModel?.forceDrop!!
        viewModel?.messages?.observe(this){outerIt->
            val items = outerIt.map { MessageItem(it.text, it.time, it.isChecked, it.senderId, it.receiverId, chatId==it.receiverId) }
            adapter.messages = items
        }
        initViews()
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
                binding.messagesRv.smoothScrollToPosition(adapter.itemCount - 1)
            }

        }
    }

    private fun initViews(){
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

    }

    companion object{
        private  const val TAG = "CHAT_ACTIVITY"
    }
}