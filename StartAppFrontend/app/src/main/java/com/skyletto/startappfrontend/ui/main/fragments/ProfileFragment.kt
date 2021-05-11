package com.skyletto.startappfrontend.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.ProfileViewModelFactory
import com.skyletto.startappfrontend.ui.main.viewmodels.ProfileViewModel

class ProfileFragment(private val id: Long) : Fragment() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(activity?.application!!, id)).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long): ProfileFragment {
            return ProfileFragment(id)
        }
    }
}