package com.example.graduationpartyinvitation;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Contact extends AppCompatActivity implements SensorEventListener {
    private TextView proximityTextView;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    private boolean isCallOngoing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        proximityTextView = findViewById(R.id.proximityTextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void OpenInstagram1(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.instagram.com/vk_lynn/"));
        startActivity(intent);
    }

    public void OpenCall1(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0182896587"));
        startActivity(intent);

        // Set call ongoing flag to true
        isCallOngoing = true;
    }

    public void OpenInstagram2(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.instagram.com/sh.dian_/"));
        startActivity(intent);
    }

    public void OpenCall2(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:01114897992"));
        startActivity(intent);

        // Set call ongoing flag to true
        isCallOngoing = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY && isCallOngoing) {
            float proximityValue = event.values[0];
            if (proximityValue < proximitySensor.getMaximumRange()) {
                // Object is near
                proximityTextView.setText("Proximity: Near");
                if (wakeLock == null) {
                    wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, getLocalClassName());
                    wakeLock.acquire();
                }
            } else {
                // Object is far
                proximityTextView.setText("Proximity: Far");
                if (wakeLock != null && wakeLock.isHeld()) {
                    wakeLock.release();
                    wakeLock = null;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }
}