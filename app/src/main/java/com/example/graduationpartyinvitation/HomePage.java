package com.example.graduationpartyinvitation;

import android.Manifest;
import android.annotation.SuppressLint;
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

    ImageView mReserve, mQR, mLocation, mFeedback, mCall;
    TextView calendarTextView, coordinatesTextView;

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
        mFeedback = findViewById(R.id.feedback);
        mCall = findViewById(R.id.contact);
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

        //feedback
        mFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, FeedbackForm.class);
            startActivity(intent);
        });

        //Contact
        mCall.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Contact.class);
            startActivity(intent);
        });

        calendarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });

        // Initialize LocationManager and LocationListener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        // Check if the ACCESS_FINE_LOCATION permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it has not been granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Start receiving location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    // Handle the permission request response
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALENDAR_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCalendar();
            } else {
                Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Start receiving location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
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

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Display or use the latitude and longitude values as per your requirement
            coordinatesTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Called when the provider status changes (for example, GPS is enabled or disabled)
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Called when the GPS provider is enabled by the user
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Called when the GPS provider is disabled by the user
        }
    }
}
