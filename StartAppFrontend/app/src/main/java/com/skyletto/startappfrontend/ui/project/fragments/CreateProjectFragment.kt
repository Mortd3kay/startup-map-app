package com.skyletto.startappfrontend.ui.project.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.ProfileViewModelFactory
import com.skyletto.startappfrontend.common.utils.paintButtonText
import com.skyletto.startappfrontend.databinding.ActivitySettingsBindingImpl
import com.skyletto.startappfrontend.databinding.FragmentCreateProjectBinding
import com.skyletto.startappfrontend.ui.main.viewmodels.MessagesViewModel
import com.skyletto.startappfrontend.ui.project.viewmodels.CreateProjectViewModel

class CreateProjectFragment : Fragment() {
    private lateinit var binding: FragmentCreateProjectBinding
    private var viewModel: CreateProjectViewModel? = null
    private val sp = activity?.getSharedPreferences("profile", Activity.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it, ProfileViewModelFactory(it.application, getIdFromSP())).get(CreateProjectViewModel::class.java) }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_project, container, false)
        initViews()
        return binding.root
    }

    private fun initViews(){
        paintButtonText(binding.projectAddRoleBtn)
    }

    private fun getIdFromSP() = sp?.getLong("id", -1)!!

    companion object {

        @JvmStatic
        fun newInstance() = CreateProjectFragment()
    }
}