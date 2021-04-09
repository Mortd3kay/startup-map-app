package com.skyletto.startappfrontend.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.main.fragments.MapsFragment;
import com.skyletto.startappfrontend.ui.main.fragments.ProfileFragment;
import com.skyletto.startappfrontend.ui.start.StartActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements ActivityFragmentChanger {

    private static final String TAG = "MAIN_ACTIVITY";

    private FragmentManager fm;
    private final Fragment profileFragment = ProfileFragment.newInstance();
    private MapsFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String token = bundle.getString("token");
        long id = bundle.getLong("id");

        if (token == null) {
            SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
            token = sp.getString("token", "");
            if (token.equals("")) {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
            }
            id = sp.getLong("id", -1);
        }

        fm = getSupportFragmentManager();

        mapFragment = (MapsFragment) fm.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), MapsFragment.class.getName());
        mapFragment.setActivity(this);
        fm.beginTransaction().replace(R.id.main_pane,mapFragment).commitNow();
    }

    @Override
    public void goToProfile() {
        fm.beginTransaction().replace(R.id.main_pane, profileFragment).addToBackStack("profile").commit();
    }
}