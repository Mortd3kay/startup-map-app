package com.skyletto.startappfrontend.ui.auth.fragments;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.databinding.FragmentSecondStepBinding;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel;
import com.skyletto.startappfrontend.utils.LaconicTextWatcher;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SecondStepFragment extends Fragment {

    private static final String TAG = "SECOND_AUTH_STEP_FRAGMENT";

    private ActivityStepper mActivity;
    private SharedAuthViewModel viewModel;

    private TextWatcher personalInfoWatcher;

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
        personalInfoWatcher = (LaconicTextWatcher) s -> viewModel.checkPersonalInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSecondStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_step, container, false);
        View v = binding.getRoot();
        binding.setModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.authRegisterFirstNameInput.addTextChangedListener(personalInfoWatcher);
        binding.authRegisterSecondNameInput.addTextChangedListener(personalInfoWatcher);

        return v;
    }
}