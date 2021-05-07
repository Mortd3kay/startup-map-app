package com.skyletto.startappfrontend.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.databinding.ActivityChatBinding
import com.skyletto.startappfrontend.ui.settings.SettingsActivity

class ChatActivity : AppCompatActivity() {
    private var viewModel: ChatViewModel? = null
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        initViews()
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        binding.model = viewModel
        val chatId = intent.extras?.getLong("id")
        viewModel?.chatId = chatId

    }

    private fun initViews(){
        binding.chatTbSettings.setOnClickListener { startActivity(Intent(this@ChatActivity, SettingsActivity::class.java)) }
        binding.chatBackBtn.setOnClickListener { onBackPressed() }
    }
}