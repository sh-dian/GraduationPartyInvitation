package com.example.graduationpartyinvitation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Assign the provided GoogleMap instance to the member variable mMap
        mMap = googleMap;

        // Create a LatLng object with the desired latitude and longitude values
        LatLng latLng = new LatLng(3.5074244876264133, 103.39464588895557); // coordinates

        // Create a MarkerOptions object and set its position and title
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + "  " + latLng.longitude);

        // Clear any existing markers on the map
        mMap.clear();

        // Animate the camera to the specified LatLng position and set the desired zoom level
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        // Add the marker to the map
        mMap.addMarker(markerOptions);
    }
}