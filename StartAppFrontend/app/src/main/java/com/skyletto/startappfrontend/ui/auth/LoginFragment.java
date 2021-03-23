package com.skyletto.startappfrontend.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyletto.startappfrontend.R;

import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    ActivityStepper mActivity;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        return v;
    }

    public void nextStep(){
        mActivity.nextStep();
    }

    public void prevStep(){
        mActivity.prevStep();
    }
}