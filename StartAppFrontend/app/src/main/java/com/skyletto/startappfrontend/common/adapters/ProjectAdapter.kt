package com.skyletto.startappfrontend.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColorStateList
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.models.ProjectWithTagsAndRoles
import com.skyletto.startappfrontend.databinding.ProjectItemBinding
import com.skyletto.startappfrontend.domain.entities.Role
import com.skyletto.startappfrontend.domain.entities.Tag

class ProjectAdapter(val context: Context,private val roleTypes: List<Role>) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    var onDeleteProjectListener: OnDeleteProjectListener? = null

    var projects = listOf<ProjectWithTagsAndRoles>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var expanded = false
        val adapter = RoleInProjectAdapter(context, roleTypes)
        val binding = DataBindingUtil.bind<ProjectItemBinding>(itemView)
        init {
            binding?.projectItemListView?.adapter = adapter
        }
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

            projects[position].tags?.let { it1 -> inflateChipGroup(it.projectItemChipGroup, it1) }
            it.projectItemDeleteBtn.setOnClickListener { onDeleteProjectListener?.delete(projects[position].project) }
        }

    }

    private fun inflateChipGroup(group: ChipGroup, tags: Set<Tag>) {
        group.removeAllViews()
        for (t in tags) {
            val chip = (Chip(context))
            chip.text = t.name
            chip.chipBackgroundColor = getColorStateList(context.resources, R.color.pink2, null)
            group.addView(chip)
        }
    }

    override fun getItemCount() = projects.size
}