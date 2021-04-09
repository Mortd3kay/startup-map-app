package com.skyletto.startappfrontend.ui.start;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.auth.AuthActivity;
import com.skyletto.startappfrontend.ui.main.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "START_ACTIVITY";
    private boolean auth;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String token = sp.getString("token", "");
        long id = sp.getLong("id", -1);
        auth  = !token.equals("");

        new Handler().postDelayed(() -> {
            if (auth){
                bundle.putString("token", token);
                bundle.putLong("id", id);
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                View v = findViewById(R.id.start_logo_img);
                Intent intent = new Intent(StartActivity.this, AuthActivity.class);
                if (v != null) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this, v, getString(R.string.start_auth_image_transition));
                    bundle = options.toBundle();
                }
                if (bundle == null)
                    startActivity(intent);
                else startActivity(intent, bundle);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        new Handler().postDelayed(this::finish, 1000);
    }
}