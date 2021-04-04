package com.skyletto.startappfrontend.ui.start;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.auth.AuthActivity;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(() -> {
            View v = findViewById(R.id.start_logo_img);
            Intent intent = new Intent(StartActivity.this, AuthActivity.class);
            Bundle bundle = null;
            if (v != null) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this, v, getString(R.string.start_auth_image_transition));
                bundle = options.toBundle();
            }
            if (bundle==null)
                startActivity(intent);
            else startActivity(intent, bundle);
        }, 1000);
        new Handler().postDelayed(this::finish, 2000);
    }
}