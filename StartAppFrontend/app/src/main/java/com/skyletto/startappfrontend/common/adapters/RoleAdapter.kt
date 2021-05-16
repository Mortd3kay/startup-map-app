package com.skyletto.startappfrontend.common.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.models.ProjectRoleItem
import com.skyletto.startappfrontend.databinding.RoleItemBinding
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.ProjectRole

class RoleAdapter(private val context: Context, private val roleTypes: List<ProjectRole>) : RecyclerView.Adapter<RoleAdapter.RoleViewHolder>() {

    var roles = mutableListOf<ProjectRoleItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addRole(project: Project) {
        val role = ProjectRoleItem()
        role.project = project
        roles.add(role)
        Log.d(TAG, "addRole: $role")
        notifyDataSetChanged()
    }

    inner class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RoleItemBinding? = DataBindingUtil.bind(itemView)

        init {
            binding?.let {
                it.roleSpinner.adapter = ArrayAdapter(itemView.context, R.layout.support_simple_spinner_dropdown_item, roleTypes)
                it.roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        it.item?.role?.set(it.roleSpinner.selectedItem as ProjectRole)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
                it.roleSpinner.setSelection((Math.random()*roleTypes.size).toInt())
                it.click = View.OnClickListener {
                    roles.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
                it.roleSalaryType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, _position: Int, id: Long) {
                        val char = (it.roleSalaryType.selectedItem as String)[0]
                        it.item?.salaryType?.set(char)
                        it.roleSalaryAmount.let { innerIt ->
                            if (char == '%' && innerIt.text.length > 2) {
                                innerIt.setText(innerIt.text.substring(0, 2))
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val binding = RoleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoleViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.binding?.item = roles[position]
    }

    override fun getItemCount() = roles.size

    companion object {
        const val TAG = "ROLE_ADAPTER"
    }
}