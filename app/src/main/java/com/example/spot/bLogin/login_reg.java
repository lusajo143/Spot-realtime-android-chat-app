package com.example.spot.bLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.spot.R;

public class login_reg extends AppCompatActivity {

    private TextView dont_have_acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_reg);

        dont_have_acc = findViewById(R.id.to_register);


        dont_have_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_reg.this, reg.class);
                startActivity(intent);
            }
        });

    }
}