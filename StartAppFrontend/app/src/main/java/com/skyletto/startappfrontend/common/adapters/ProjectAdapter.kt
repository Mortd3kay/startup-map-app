package com.skyletto.startappfrontend.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.common.models.ProjectWithTagsAndRoles
import com.skyletto.startappfrontend.databinding.ProjectItemBinding

class ProjectAdapter : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    var onDeleteProjectListener: OnDeleteProjectListener? = null

    var projects = listOf<ProjectWithTagsAndRoles>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var expanded = false
        val binding = DataBindingUtil.bind<ProjectItemBinding>(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.binding?.let {
            it.project = projects[position]
            it.click = View.OnClickListener { _ ->
                if (holder.expanded){
                    it.projectItemDescription.maxLines = 4
                    it.projectItemListView.visibility = View.GONE
                    it.projectItemDeleteBtn.visibility = View.GONE
                    holder.expanded = false
                } else {
                    it.projectItemDeleteBtn.visibility = View.VISIBLE
                    it.projectItemListView.visibility = View.VISIBLE
                    it.projectItemDescription.maxLines = Int.MAX_VALUE
                    holder.expanded = true
                }
            }
            it.projectItemDeleteBtn.setOnClickListener { onDeleteProjectListener?.delete(projects[position].project) }
        }

    }

    override fun getItemCount() = projects.size
}