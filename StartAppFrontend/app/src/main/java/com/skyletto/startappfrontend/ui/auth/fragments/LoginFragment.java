package com.skyletto.startappfrontend.ui.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.viewmodels.LoginAuthViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class LoginFragment extends Fragment {

    private ActivityStepper mActivity;
    private LoginAuthViewModel viewModel;

    private EditText email;
    private EditText password;
    private MaterialButton loginBtn;

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
        viewModel = new ViewModelProvider(getActivity()).get(LoginAuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        email = (EditText) v.findViewById(R.id.auth_email_input_field);
        password = (EditText) v.findViewById(R.id.auth_password_input_field);
        loginBtn = (MaterialButton) v.findViewById(R.id.auth_login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setEmail(email.getText().toString());
                viewModel.setPassword(password.getText().toString());
                viewModel.login(mActivity);
            }
        });

        return v;
    }

    public void nextStep(){
        mActivity.nextStep();
    }

    public void prevStep(){
        mActivity.prevStep();
    }
}