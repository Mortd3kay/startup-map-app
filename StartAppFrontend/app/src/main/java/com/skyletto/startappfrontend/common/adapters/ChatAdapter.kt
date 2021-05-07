package com.skyletto.startappfrontend.common.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.common.ChatItem
import com.skyletto.startappfrontend.common.utils.OnChatClickListener
import com.skyletto.startappfrontend.databinding.ChatItemBinding
import com.skyletto.startappfrontend.ui.chat.ChatActivity
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class ChatAdapter(val context: Context) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var chats: MutableList<ChatItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addChat(chat: ChatItem) {
        chats.add(chat)
        notifyDataSetChanged()
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ChatItemBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding?.model = chats[position]
        holder.binding?.click = object: OnChatClickListener{
            override fun onClick(chatId: Long) {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("id", chatId)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount() = chats.size

    companion object{

        private const val TAG = "CHAT_ADAPTER"

        @JvmStatic
        @SuppressLint("UseCompatLoadingForDrawables")
        @BindingAdapter("imageUrl", requireAll = false)
        fun loadImage(imageView: ImageView, v: Int?) {
            v?.let {imageView.setImageDrawable(imageView.resources.getDrawable(it, null)) }
        }

        @JvmStatic
        @BindingAdapter("time", requireAll = false)
        fun convertTime(textView: TextView, v: String?) {
            v?.let {
                try {
                    val ldt = LocalDateTime.parse(it)
                    Log.d(TAG, "convertTime: $ldt")
                    val zUtc = ldt.atZone(ZoneId.of("UTC"))
                    Log.d(TAG, "convertTime: $zUtc")
                    textView.text = zUtc.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                } catch (e: Exception){
                    Log.e(TAG, "convertTime: ", e.fillInStackTrace())
                    textView.text = v
                }

            }
        }
    }


}