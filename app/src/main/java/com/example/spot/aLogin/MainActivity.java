package com.example.spot.aLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.spot.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main2);
    }
}