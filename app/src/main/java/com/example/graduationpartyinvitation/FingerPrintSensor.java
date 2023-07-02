package com.example.graduationpartyinvitation;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FingerPrintSensor extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private ImageView fingerPrint;
    private AnimatorSet animatorSet;

    //sensor
    private FingerprintManager fingerprintManager;
    private FingerPrintHelper fingerprintHelper;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_sensor);

        fingerPrint = findViewById(R.id.fingerPrint);
        // Load the fade animation from XML
        animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.fade_animation);

        //fingerprint sensor
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        fingerprintHelper = new FingerPrintHelper( this,this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT},
                    PERMISSION_REQUEST_CODE);
        } else {
            startFingerprintAuth();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimation();
    }

    private void startAnimation() {
        // Apply the animation to the Fingerprint image
        animatorSet.setTarget(fingerPrint);
        animatorSet.start();
    }

    private void stopAnimation() {
        animatorSet.cancel();
    }

    //fingerprint sensor
    private void startFingerprintAuth() {
        if (fingerprintManager.isHardwareDetected()) {
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "No fingerprint registered on this device.", Toast.LENGTH_SHORT).show();
            } else {
                fingerprintHelper.startAuth(fingerprintManager, this); // Pass the activity context
            }
        } else {
            Toast.makeText(this, "Your device not supported Fingerprint sensor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFingerprintAuth();
            } else {
                Toast.makeText(this, "Fingerprint permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //if the sensor succeed, it navigate to display reservation page
    public void navigateToNextPage() {
        try {
            Intent intent = new Intent(FingerPrintSensor.this, DisplayReservation.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}