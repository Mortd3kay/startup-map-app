package com.skyletto.startappfrontend.common.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.models.ProjectRoleItem
import com.skyletto.startappfrontend.databinding.RoleItemBinding
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.ProjectRole
import com.skyletto.startappfrontend.ui.project.fragments.CreateProjectFragment

class RoleAdapter(private val context: Context, private val roleTypes: List<ProjectRole>) : RecyclerView.Adapter<RoleAdapter.RoleViewHolder>() {

    var roles = mutableListOf<ProjectRoleItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    fun addRole(project:Project){
        val role = ProjectRoleItem()
        role.project = project
        roles.add(role)
        Log.d(TAG, "addRole: $role")
        notifyDataSetChanged()
    }

    class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RoleItemBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val binding = RoleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoleViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.binding?.roleSpinner?.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, roleTypes.map { it.name })
    }

    override fun getItemCount() = roles.size

    companion object{
        const val TAG = "ROLE_ADAPTER"
    }
}