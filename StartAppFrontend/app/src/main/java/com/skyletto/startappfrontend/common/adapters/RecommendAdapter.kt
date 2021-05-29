package com.skyletto.startappfrontend.common.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.common.models.RecommendationItem
import com.skyletto.startappfrontend.databinding.RecItemBinding
import com.skyletto.startappfrontend.ui.chat.ChatActivity
import com.skyletto.startappfrontend.ui.main.fragments.OnRemoveClickListener
import com.skyletto.startappfrontend.ui.main.fragments.OnUsernameClickListener

class RecommendAdapter(val items: List<RecommendationItem>, val context:Context) : RecyclerView.Adapter<RecommendAdapter.RecommendHolder>() {

    var onUsernameClickListener: OnUsernameClickListener? = null
    var onRemoveClickListener: OnRemoveClickListener? = null

    inner class RecommendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding:RecItemBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendHolder {
        val binding = RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecommendHolder, position: Int) {
        holder.binding?.item = items[position]
        holder.binding?.item?.let {
            holder.binding.click = View.OnClickListener { _ ->
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("id", it.userId)
                context.startActivity(intent)
            }
            holder.binding.usernameClick = View.OnClickListener { _->
                onUsernameClickListener?.onClick(it.userId)
            }

            holder.binding.removeClick = View.OnClickListener { _->
                onRemoveClickListener?.onClick(it.id, it.isProject)
            }
        }

    }

    override fun getItemCount() = items.size
}