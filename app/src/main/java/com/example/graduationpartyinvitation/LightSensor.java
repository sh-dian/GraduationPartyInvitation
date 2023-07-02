package com.example.graduationpartyinvitation;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LightSensor extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor lightSensor;
    TextView sensorView;

    ProgressBar progressBar;
    Handler handler;
    Runnable progressRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);

        // Initialize the sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Get the light sensor
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Initialize the TextView
        sensorView = findViewById(R.id.sensorView);
        progressBar = findViewById(R.id.progressBar);
        handler = new Handler();

        progressRunnable = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

                Intent intent = new Intent(LightSensor.this, HomePage.class);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        progressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(progressRunnable, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];

            // Check if light value is high
            if (lightValue > 10) { // You can adjust this threshold value as per your requirement
                sensorView.setText("It's bright outside, have a good day!");
            }else{
                sensorView.setText("It's dark outside, don't forget your umbrella");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
