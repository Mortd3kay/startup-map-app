package com.skyletto.startappfrontend.ui.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.databinding.FragmentThirdStepBinding;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentThirdStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_third_step, container, false);
        binding.setModel(viewModel);
        View v = binding.getRoot();
        return v;
    }
}