package com.skyletto.startappfrontend.ui.auth.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.databinding.FragmentSecondStepBinding
import com.skyletto.startappfrontend.ui.auth.ActivityStepper
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel
import com.skyletto.startappfrontend.utils.LaconicTextWatcher
import com.skyletto.startappfrontend.utils.paintButtonText

class SecondStepFragment : Fragment() {
    private var mActivity: ActivityStepper? = null
    private var viewModel: SharedAuthViewModel? = null
    private var personalInfoWatcher: TextWatcher? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it).get(SharedAuthViewModel::class.java) }
        personalInfoWatcher = object : LaconicTextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel?.checkPersonalInfo()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSecondStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_step, container, false)
        val v = binding.root
        binding.model = viewModel
        binding.lifecycleOwner = this
        binding.authRegisterFirstNameInput.addTextChangedListener(personalInfoWatcher)
        binding.authRegisterSecondNameInput.addTextChangedListener(personalInfoWatcher)
        val btn = binding.secStepContBtn
        paintButtonText(btn)
        return v
    }

    companion object {
        private const val TAG = "SECOND_AUTH_STEP_FRAGMENT"
        fun newInstance(activityStepper: ActivityStepper?): SecondStepFragment {
            val fragment = SecondStepFragment()
            fragment.mActivity = activityStepper
            return fragment
        }
    }
}