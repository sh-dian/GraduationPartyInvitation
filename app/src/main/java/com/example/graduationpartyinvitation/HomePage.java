package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    ImageView mReserve, mQR, mLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mReserve = findViewById(R.id.reserveForm);
        mQR = findViewById(R.id.qr);
        mLocation = findViewById(R.id.location);

        //reservation
        mReserve.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ReservationForm.class);
            startActivity(intent);
        });

        //scan qr
        mQR.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, QRScanner.class);
            startActivity(intent);
        });

        //map
        mLocation.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ReservationForm.class);
            startActivity(intent);
        });
    }
}