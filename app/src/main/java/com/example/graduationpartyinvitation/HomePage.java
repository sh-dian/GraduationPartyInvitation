package com.example.graduationpartyinvitation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomePage extends AppCompatActivity {

    ImageView mReserve, mQR, mLocation;
    TextView calendarTextView,coordinatesTextView;

    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int REQUEST_CALENDAR_PERMISSION = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mReserve = findViewById(R.id.reserveForm);
        mQR = findViewById(R.id.qr);
        mLocation = findViewById(R.id.location);
        coordinatesTextView = findViewById(R.id.coordinatesTextView);
        calendarTextView = findViewById(R.id.calendarTextView);

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
            Intent intent = new Intent(HomePage.this, Map.class);
            startActivity(intent);
        });

        calendarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });


    }

    // Handle the permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALENDAR_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCalendar();
            } else {
                Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCalendar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("content://com.android.calendar/time"));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CALENDAR_PERMISSION);
        }
    }
}