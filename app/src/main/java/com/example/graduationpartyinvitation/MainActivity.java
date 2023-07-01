package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView mRegister;
    Button mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegister = findViewById(R.id.registerNow);
        mRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationForm.class);
            startActivity(intent);
        });

        mLogin = findViewById(R.id.login);
        mLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
        });
    }
}