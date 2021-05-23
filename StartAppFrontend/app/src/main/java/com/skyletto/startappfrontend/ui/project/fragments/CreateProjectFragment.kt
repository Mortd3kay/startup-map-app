package com.skyletto.startappfrontend.ui.project.fragments

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.adapters.RoleAdapter
import com.skyletto.startappfrontend.common.utils.*
import com.skyletto.startappfrontend.databinding.FragmentCreateProjectBinding
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.ui.project.viewmodels.CreateProjectViewModel

class CreateProjectFragment : Fragment() {
    private lateinit var binding: FragmentCreateProjectBinding
    private var viewModel: CreateProjectViewModel? = null
    private var sp :SharedPreferences? = null
    private var adapter: RoleAdapter? = null
    private lateinit var locationFragment:GetLocationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it, ProjectViewModelFactory(it.application, getIdFromSP())).get(CreateProjectViewModel::class.java) }
        viewModel?.let { locationFragment = GetLocationFragment(it) }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_project, container, false)
        binding.model = viewModel
        initViews()
        return binding.root
    }

    private fun initViews(){
        rolePart()
        buttonPart()
        tagPart()
    }

    private fun tagPart() {
        viewModel?.let {
            it.chosenTags.observe(viewLifecycleOwner, { tags: Set<Tag> -> inflateChipGroup(binding.projectThirdStepEntryChipGroup, tags, 1) })
            it.tags.observe(viewLifecycleOwner, { tags: Set<Tag> -> inflateChipGroup(binding.projectThirdStepChooseChipGroup, tags, 2) })
        }
        binding.projectTagNameInput.addTextChangedListener(object : LaconicTextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim { it <= ' ' }.isEmpty()) viewModel?.loadRandomTags()
                if (s.toString().trim { it <= ' ' }.length < 2) return
                viewModel?.loadSimilarTags(s.toString())
            }
        })
    }

    private fun rolePart(){
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
    }

    private fun buttonPart(){
        binding.projectLocationInput.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.project_frame,locationFragment).addToBackStack("MAP").commit()
        }
        binding.createProjectBackBtn.setOnClickListener { activity?.onBackPressed() }
        binding.createProjectOkBtn.setOnClickListener {
            if (viewModel?.checkLatLng()==true && viewModel?.checkTitleAndDescription()==true){
                it.isClickable = false
                adapter?.roles?.let { it1 -> viewModel?.prepareProject(it1) }
                viewModel?.saveProject(
                        {
                            toast(context, getString(R.string.cant_save_project))
                            it.isClickable = true
                        },
                        {
                            activity?.finish()
                        }
                )

            } else if (viewModel?.checkLatLng()==false){
                toast(context, getString(R.string.place_is_necessary))
            } else toast(context, getString(R.string.fields_are_empty))
        }
    }

    private fun getIdFromSP():Long {
        sp = activity?.getSharedPreferences("profile", Activity.MODE_PRIVATE)
        return sp?.getLong("id", -1)!!
    }

    private fun inflateChipGroup(group: ChipGroup, tags: Set<Tag>, flag: Int) {
        group.removeAllViews()
        for (t in tags) {
            val chip = (Chip(requireContext()))
            chip.text = t.name
            if (flag == 1) {
                chip.isChecked = true
                chip.isCloseIconVisible = true
                chip.setOnClickListener { viewModel?.toTagFromChosenTag(t) }
            } else if (flag == 2) {
                chip.setBackgroundColor(Color.BLUE)
                chip.setOnClickListener { viewModel?.toChosenTagFromTag(t) }
            }
            group.addView(chip)
        }
    }

    companion object {
        private const val TAG = "CREATE_PROJECT_FRAGMENT"
        @JvmStatic
        fun newInstance() = CreateProjectFragment()
    }
}