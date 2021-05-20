package com.skyletto.startappfrontend.common.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.models.UserItem
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.common.utils.paintButtonText
import com.skyletto.startappfrontend.databinding.RoleInProjectItemBinding
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole

class RoleInProjectAdapter(val context: Context) : RecyclerView.Adapter<RoleInProjectAdapter.RoleViewHolder>() {

    var users: List<UserWithTags> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var roles = listOf<ProjectAndRole>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    var onAssignClickListener: OnAssignClickListener? = null

    inner class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = DataBindingUtil.bind<RoleInProjectItemBinding>(itemView)
        var selectedItem: UserItem? = null
        init {
            binding?.assignBtn?.let { paintButtonText(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val binding = RoleInProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoleViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.binding?.let { it1 ->
            it1.role = roles[position]
            it1.roleInProjectItemUsername.setAdapter(ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, users.map { UserItem(it.user.id!!, it.user.firstName +" "+ it.user.secondName,it.user.title?:"") }))
            it1.roleInProjectItemUsername.setOnItemClickListener { _, _, position, _ ->
                holder.selectedItem = it1.roleInProjectItemUsername.adapter.getItem(position) as UserItem
                Log.d(TAG, "item: ${holder.selectedItem?.id} ${holder.selectedItem?.fullName}")
            }
            it1.assignBtn.setOnClickListener {
                it1.role?.let { it2-> onAssignClickListener?.assign(it2, holder.selectedItem) }
            }
        }


    }

    override fun getItemCount() = roles.size

    companion object{
        private const val TAG = "ROLE_IN_PROJECT_ADAPTER"
    }
}