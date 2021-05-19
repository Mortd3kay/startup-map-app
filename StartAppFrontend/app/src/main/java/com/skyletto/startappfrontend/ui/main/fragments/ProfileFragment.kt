package com.skyletto.startappfrontend.ui.main.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColorStateList
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.adapters.OnDeleteProjectListener
import com.skyletto.startappfrontend.common.adapters.ProjectAdapter
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.common.utils.ProfileViewModelFactory
import com.skyletto.startappfrontend.databinding.FragmentProfileBinding
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.ui.main.viewmodels.ProfileViewModel
import com.skyletto.startappfrontend.ui.settings.SettingsActivity

class ProfileFragment(private val id: Long) : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private var adapter: ProjectAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(activity?.application!!, id)).get(ProfileViewModel::class.java)
        adapter = context?.let { ProjectAdapter(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        takeViewModel()
        initViews()
        return binding.root
    }

    private fun takeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) {
            it?.tags?.let { iIt -> inflateChipGroup(binding.profileTagsChipGroup, iIt) }
        }
        viewModel.projects.observe(viewLifecycleOwner) {
            adapter?.projects = it
        }
        adapter?.users = viewModel.knownUsers
    }

    private fun initViews() {
        binding.model = viewModel
        binding.profileTbSettings.setOnClickListener { startActivity(Intent(this.context, SettingsActivity::class.java)) }
        binding.profileProjectsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.profileProjectsRv.adapter = adapter
        adapter?.onDeleteProjectListener = object : OnDeleteProjectListener {
            override fun delete(project: Project) {
                viewModel.deleteProject(project)
            }
        }
    }

    private fun inflateChipGroup(group: ChipGroup, tags: Set<Tag>) {
        group.removeAllViews()
        for (t in tags) {
            val chip = (Chip(requireContext()))
            chip.text = t.name
            chip.chipBackgroundColor = getColorStateList(resources, R.color.skin2, null)
            group.addView(chip)
        }
    }

    companion object {
        private const val TAG = "PROFILE_FRAGMENT"

        @JvmStatic
        fun newInstance(id: Long): ProfileFragment {
            return ProfileFragment(id)
        }
    }
}