package com.skyletto.startappfrontend.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MessageItem
import com.skyletto.startappfrontend.common.adapters.MessagesAdapter
import com.skyletto.startappfrontend.databinding.ActivityChatBinding
import com.skyletto.startappfrontend.ui.settings.SettingsActivity

class ChatActivity : AppCompatActivity() {
    private var viewModel: ChatViewModel? = null
    private lateinit var binding: ActivityChatBinding
    private val adapter = MessagesAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        initViews()
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        binding.model = viewModel
        val chatId = intent.extras?.getLong("id")
        viewModel?.chatId = chatId
        viewModel?.messages?.observe(this){outerIt->
            val items = outerIt.map { MessageItem(it.text, it.time, it.isChecked, it.senderId, it.receiverId, chatId==it.receiverId) }
            adapter.messages = items
        }
    }

    private fun initViews(){
        binding.chatTbSettings.setOnClickListener { startActivity(Intent(this@ChatActivity, SettingsActivity::class.java)) }
        binding.chatBackBtn.setOnClickListener { onBackPressed() }
        binding.messagesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.messagesRv.adapter = adapter
        binding.chatSendBtn.setOnClickListener {
            viewModel?.sendMessage() }
    }

    companion object{
        private  const val TAG = "CHAT_ACTIVITY"
    }
}