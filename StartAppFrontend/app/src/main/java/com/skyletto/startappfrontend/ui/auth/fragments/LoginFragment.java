package com.skyletto.startappfrontend.ui.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.databinding.FragmentLoginBinding;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.TokenSaver;
import com.skyletto.startappfrontend.ui.auth.viewmodels.LoginAuthViewModel;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class LoginFragment extends Fragment {

    public ActivityStepper mActivity;
    private LoginAuthViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(ActivityStepper activity) {
        LoginFragment fragment = new LoginFragment();
        fragment.mActivity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginAuthViewModel.class);

        viewModel.setOnErrorLoginListener(() -> {
            Toast.makeText(getContext(), "Неверные данные", Toast.LENGTH_SHORT).show();
        });
        viewModel.setOnSuccessLoginListener(pr -> {
            Bundle b = new Bundle();
            b.putString("token",pr.getToken());
            b.putLong("id",pr.getUser().getId());
            mActivity.onFinish(b);
        });
        viewModel.setOnSaveProfileListener(pr -> {
            if (mActivity instanceof TokenSaver)
                ((TokenSaver) mActivity).save(pr.getToken(), pr.getUser().getId());
        });
        viewModel.setGoToRegister(() -> mActivity.nextStep());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.setModel(viewModel);
        View v = binding.getRoot();
        return v;
    }

}