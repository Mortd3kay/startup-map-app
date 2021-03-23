package com.skyletto.startappfrontend.ui.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.databinding.FragmentSecondStepBinding;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SecondStepFragment extends Fragment {

    private ActivityStepper mActivity;
    private SharedAuthViewModel viewModel;

    public SecondStepFragment() {
        // Required empty public constructor
    }

    public static SecondStepFragment newInstance(ActivityStepper activityStepper) {
        SecondStepFragment fragment = new SecondStepFragment();
        fragment.mActivity = activityStepper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SharedAuthViewModel.class);
        viewModel.setOnNextStepListener(() -> mActivity.nextStep());
        viewModel.setOnPrevStepListener(() -> mActivity.prevStep());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSecondStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_step, container, false);
        binding.setModel(viewModel);
        View v = binding.getRoot();
        return v;
    }
}