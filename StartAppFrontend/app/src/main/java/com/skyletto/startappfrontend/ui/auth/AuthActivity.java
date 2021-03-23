package com.skyletto.startappfrontend.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.auth.fragments.LoginFragment;
import com.skyletto.startappfrontend.ui.main.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AuthActivity extends AppCompatActivity implements ActivityStepper {

    private static final String TAG = "AUTH_ACTIVITY";

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.auth_frame_layout, LoginFragment.newInstance(this)).commit();
    }

    @Override
    public void nextStep() {

    }

    @Override
    public void prevStep() {

    }

    @Override
    public void onFinish() {
        startActivity(new Intent(AuthActivity.this, MainActivity.class));
        finish();
    }
}