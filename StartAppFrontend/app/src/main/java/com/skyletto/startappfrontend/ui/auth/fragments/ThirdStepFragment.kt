package com.skyletto.startappfrontend.ui.auth.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.LaconicTextWatcher
import com.skyletto.startappfrontend.common.utils.paintButtonText
import com.skyletto.startappfrontend.databinding.FragmentThirdStepBinding
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.ui.auth.ActivityStepper
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel

class ThirdStepFragment : Fragment() {
    private var mActivity: ActivityStepper? = null
    private var viewModel: SharedAuthViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it).get(SharedAuthViewModel::class.java) }
        viewModel?.loadRandomTags()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentThirdStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_third_step, container, false)
        binding.model = viewModel
        val btn = binding.thirdStepContBtn
        paintButtonText(btn)
        viewModel?.let {
            it.chosenTags.observe(viewLifecycleOwner, { tags: Set<Tag> -> inflateChipGroup(binding.authThirdStepEntryChipGroup, tags, 1) })
            it.tags.observe(viewLifecycleOwner, { tags: Set<Tag> -> inflateChipGroup(binding.authThirdStepChooseChipGroup, tags, 2) })
        }
        binding.authTagNameInput.addTextChangedListener(object : LaconicTextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim { it <= ' ' }.isEmpty()) viewModel?.loadRandomTags()
                if (s.toString().trim { it <= ' ' }.length < 2) return
                viewModel?.loadSimilarTags(s.toString())
            }
        })
        return binding.root
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
                chip.chipBackgroundColor = ResourcesCompat.getColorStateList(resources, R.color.pink2, null)
                chip.setOnClickListener { viewModel?.toChosenTagFromTag(t) }
            }
            group.addView(chip)
        }
    }

    companion object {
        fun newInstance(activityStepper: ActivityStepper?): ThirdStepFragment {
            val fragment = ThirdStepFragment()
            fragment.mActivity = activityStepper
            return fragment
        }
    }
}