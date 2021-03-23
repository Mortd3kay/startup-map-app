package com.skyletto.startappfrontend.ui.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.databinding.FragmentFirstStepBinding;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class FirstStepFragment extends Fragment {

    private ActivityStepper mActivity;
    private SharedAuthViewModel viewModel;

    public FirstStepFragment() {
        // Required empty public constructor
    }


    public static FirstStepFragment newInstance(ActivityStepper activityStepper) {
        FirstStepFragment fragment = new FirstStepFragment();
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
        FragmentFirstStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_step, container, false);
        binding.setModel(viewModel);
        View v = binding.getRoot();
        return v;
    }
}