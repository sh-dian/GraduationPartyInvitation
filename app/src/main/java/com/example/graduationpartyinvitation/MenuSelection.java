package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuSelection extends AppCompatActivity {

    Button reservation, wish, suggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_selection);

        reservation = findViewById(R.id.reservation);
        wish = findViewById(R.id.wishes);
        suggestion = findViewById(R.id.suggestion);

        reservation.setOnClickListener(v -> {
            Intent intent = new Intent(MenuSelection.this, ReservationForm.class);
            startActivity(intent);
        });

        wish.setOnClickListener(v -> {
            Intent intent = new Intent(MenuSelection.this, PartyWishesForm.class);
            startActivity(intent);
        });

        suggestion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuSelection.this, PartySuggestionForm.class);
            startActivity(intent);
        });
    }
}