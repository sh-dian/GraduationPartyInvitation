package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PartyWishesForm extends AppCompatActivity {

    private EditText wishText;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_wishes_form);

        wishText = findViewById(R.id.wishText);
        Button wishSubmit = findViewById(R.id.submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("wishes");

        wishSubmit.setOnClickListener(v -> addWish());
    }

    private void addWish() {
        String wish = wishText.getText().toString().trim();

        if (!wish.isEmpty()) {
            String wishID = databaseReference.push().getKey();

            Wishes wishObj = new Wishes(wish);

            databaseReference.child(wishID).setValue(wishObj);

            // Clear the input fields
            wishText.setText("");

            // Show success toast message
            Toast.makeText(PartyWishesForm.this, "Thanks for your Wishes!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PartyWishesForm.this, DisplayWish.class);
            startActivity(intent);
        }
    }
}