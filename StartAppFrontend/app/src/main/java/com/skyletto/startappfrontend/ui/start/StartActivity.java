package com.skyletto.startappfrontend.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.ui.main.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "START_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        startActivity(new Intent(this, MainActivity.class));



//        new Handler().postDelayed(() -> {
//            View v = findViewById(R.id.start_logo_img);
//            Intent intent = new Intent(StartActivity.this, AuthActivity.class);
//            Bundle bundle = null;
//            if (v != null) {
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this, v, getString(R.string.start_auth_image_transition));
//                bundle = options.toBundle();
//            }
//            if (bundle==null)
//                startActivity(intent);
//            else startActivity(intent, bundle);
//        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        new Handler().postDelayed(this::finish, 1000);
    }
}