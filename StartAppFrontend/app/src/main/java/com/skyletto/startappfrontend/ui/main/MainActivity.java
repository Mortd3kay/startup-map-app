package com.skyletto.startappfrontend.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import com.skyletto.startappfrontend.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

    }
}