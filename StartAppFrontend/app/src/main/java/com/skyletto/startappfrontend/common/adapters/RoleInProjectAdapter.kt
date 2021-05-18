package com.skyletto.startappfrontend.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.paintButtonText
import com.skyletto.startappfrontend.databinding.ProjectItemBinding
import com.skyletto.startappfrontend.databinding.RoleInProjectItemBinding
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole
import com.skyletto.startappfrontend.domain.entities.Role

class RoleInProjectAdapter(val context: Context,private val roleTypes: List<Role>) : RecyclerView.Adapter<RoleInProjectAdapter.RoleViewHolder>() {

    var roles = listOf<ProjectAndRole>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    inner class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = DataBindingUtil.bind<RoleInProjectItemBinding>(itemView)
        init {
            binding?.assignBtn?.let { paintButtonText(it) }
            binding?.roleInProjectItemSpinner?.adapter = ArrayAdapter(itemView.context, R.layout.support_simple_spinner_dropdown_item, roleTypes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val binding = RoleInProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoleViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.binding?.let {
            it.role = roles[position]
        }
    }

    override fun getItemCount() = roles.size
}