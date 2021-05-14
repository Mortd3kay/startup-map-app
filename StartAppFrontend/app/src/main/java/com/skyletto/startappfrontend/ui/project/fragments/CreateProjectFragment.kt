package com.skyletto.startappfrontend.ui.project.fragments

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.adapters.RoleAdapter
import com.skyletto.startappfrontend.common.utils.ProfileViewModelFactory
import com.skyletto.startappfrontend.common.utils.ProjectViewModelFactory
import com.skyletto.startappfrontend.common.utils.paintButtonText
import com.skyletto.startappfrontend.databinding.ActivitySettingsBindingImpl
import com.skyletto.startappfrontend.databinding.FragmentCreateProjectBinding
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.ui.main.viewmodels.MessagesViewModel
import com.skyletto.startappfrontend.ui.project.viewmodels.CreateProjectViewModel

class CreateProjectFragment : Fragment() {
    private lateinit var binding: FragmentCreateProjectBinding
    private var viewModel: CreateProjectViewModel? = null
    private var sp :SharedPreferences? = null
    private var adapter: RoleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it, ProjectViewModelFactory(it.application, getIdFromSP())).get(CreateProjectViewModel::class.java) }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_project, container, false)
        initViews()
        return binding.root
    }

    private fun initViews(){
        viewModel?.roles?.observe(viewLifecycleOwner){outerIt ->
            adapter = context?.let { RoleAdapter(it,outerIt) }
            binding.rolesListView.adapter = adapter
            binding.rolesListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
        paintButtonText(binding.projectAddRoleBtn)
        binding.projectAddRoleBtn.setOnClickListener {
            adapter?.let {
                if (it.itemCount < CreateProjectViewModel.ROLES_MAX_COUNT){
                    Log.d(TAG, "addRoleButton: free slot")
                    viewModel?.project?.get()?.let { it1 ->
                        it.addRole(it1)
                        Log.d(TAG, "addRoleButton: added role")
                    }
                }
            }
        }
        binding.createProjectBackBtn.setOnClickListener { activity?.onBackPressed() }
        binding.createProjectOkBtn.setOnClickListener {
        }

    }

    private fun getIdFromSP():Long {
        sp = activity?.getSharedPreferences("profile", Activity.MODE_PRIVATE)
        return sp?.getLong("id", -1)!!
    }

    companion object {
        private const val TAG = "CREATE_PROJECT_FRAGMENT"
        @JvmStatic
        fun newInstance() = CreateProjectFragment()
    }
}