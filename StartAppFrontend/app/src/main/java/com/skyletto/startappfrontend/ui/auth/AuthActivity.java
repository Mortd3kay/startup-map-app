package com.skyletto.startappfrontend.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.auth.fragments.FirstStepFragment;
import com.skyletto.startappfrontend.ui.auth.fragments.LoginFragment;
import com.skyletto.startappfrontend.ui.auth.fragments.SecondStepFragment;
import com.skyletto.startappfrontend.ui.auth.fragments.ThirdStepFragment;
import com.skyletto.startappfrontend.ui.main.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AuthActivity extends AppCompatActivity implements ActivityStepper, TokenSaver {

    private static final String TAG = "AUTH_ACTIVITY";
    private static final int STEPS_COUNT = 3;
    private static int stepNum = -1;

    private FragmentManager fm;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        stepNum = -1;
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.auth_frame_layout, LoginFragment.newInstance(this)).commit();

        fragments = new Fragment[STEPS_COUNT];
        fragments[0] = FirstStepFragment.newInstance(this);
        fragments[1] = SecondStepFragment.newInstance(this);
        fragments[2] = ThirdStepFragment.newInstance(this);
    }

    @Override
    public void nextStep() {
        fm.beginTransaction().replace(R.id.auth_frame_layout, fragments[++stepNum]).addToBackStack(null).commit();
    }

    @Override
    public void prevStep() {
        fm.popBackStack();
        stepNum--;
    }

    @Override
    public void onFinish(Bundle bundle) {
        startActivity(new Intent(AuthActivity.this, MainActivity.class), bundle);
        finish();
    }

    @Override
    public void onBackPressed() {
        stepNum--;
        super.onBackPressed();
    }

    @Override
    public void save(String token, long id) {
        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("token", token);
        e.putLong("id", id);
        e.apply();
    }
}