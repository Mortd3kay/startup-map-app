package com.skyletto.startappfrontend.ui.auth.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.databinding.FragmentThirdStepBinding;
import com.skyletto.startappfrontend.domain.entities.Tag;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel;
import com.skyletto.startappfrontend.utils.LaconicTextWatcher;

import java.util.Set;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ThirdStepFragment extends Fragment {

    private ActivityStepper mActivity;
    private SharedAuthViewModel viewModel;

    public ThirdStepFragment() {
        // Required empty public constructor
    }

    public static ThirdStepFragment newInstance(ActivityStepper activityStepper) {
        ThirdStepFragment fragment = new ThirdStepFragment();
        fragment.mActivity = activityStepper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SharedAuthViewModel.class);
        viewModel.loadRandomTags();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentThirdStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_third_step, container, false);
        binding.setModel(viewModel);
        viewModel.getChosenTags().observe(getViewLifecycleOwner(), tags -> inflateChipGroup(binding.authThirdStepEntryChipGroup, tags, 1));
        viewModel.getTags().observe(getViewLifecycleOwner(), tags -> inflateChipGroup(binding.authThirdStepChooseChipGroup, tags, 2));

        binding.authTagNameInput.addTextChangedListener((LaconicTextWatcher) s -> {
            if (s.toString().trim().length()==0) viewModel.loadRandomTags();
            if (s.toString().trim().length()<2) return;
            viewModel.loadSimilarTags(s.toString());
        });

        View v = binding.getRoot();
        return v;
    }

    private void inflateChipGroup(ChipGroup group, Set<Tag> tags, int flag) {
        group.removeAllViews();
        for (Tag t : tags) {
            Chip chip = new Chip(requireContext());
            chip.setText(t.getName());
            if (flag==1){
                chip.setChecked(true);
                chip.setCloseIconVisible(true);
                chip.setOnClickListener(v->viewModel.toTagFromChosenTag(t));
            }
            else if (flag==2){
                chip.setBackgroundColor(Color.BLUE);
                chip.setOnClickListener(v->viewModel.toChosenTagFromTag(t));
            }
            group.addView(chip);
        }
    }
}