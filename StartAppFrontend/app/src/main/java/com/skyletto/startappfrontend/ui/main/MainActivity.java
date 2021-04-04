package com.skyletto.startappfrontend.ui.main;

import android.os.Bundle;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.main.fragments.MapsFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_pane,fm.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), MapsFragment.class.getName())).commitNow();

    }
}