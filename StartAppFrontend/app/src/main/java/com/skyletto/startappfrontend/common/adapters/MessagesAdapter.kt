package com.skyletto.startappfrontend.common.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.MessageItem
import com.skyletto.startappfrontend.databinding.MessageItemBinding
import com.skyletto.startappfrontend.ui.chat.viewmodels.OnDownPositionListener
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessagesHolder>() {
    var onDownPositionListener: OnDownPositionListener? = null
    var messages: List<MessageItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MessagesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: MessageItemBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessagesHolder(binding.root)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {
        onDownPositionListener?.check(position >= itemCount-2)
        holder.binding?.model = messages[position]
        holder.binding?.messageText?.let {
            with(it) {
                if (messages[position].outcoming) {
                    background = resources?.getDrawable(R.drawable.outcoming_bubble, null)
                    gravity = Gravity.END
                    setTextColor(resources?.getColor(R.color.white)!!)
                } else {
                    background = resources?.getDrawable(R.drawable.incoming_bubble, null)
                    gravity = Gravity.START
                    setTextColor(resources?.getColor(R.color.dark)!!)
                }
            }
        }


    }

    override fun getItemCount() = messages.size

    companion object {
        private const val TAG = "MESSAGES_ADAPTER"

        @JvmStatic
        @BindingAdapter("time", requireAll = false)
        fun convertTime(textView: TextView, v: String?) {
            v?.let {
                try {
                    val ldt = LocalDateTime.parse(it)
                    val zUtc = ldt.atZone(ZoneId.of("UTC"))
                    textView.text = zUtc.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                } catch (e: Exception) {
                    Log.e(TAG, "convertTime: ", e.fillInStackTrace())
                    textView.text = v
                }

            }
        }
    }
}