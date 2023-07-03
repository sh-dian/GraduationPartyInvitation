package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayWish extends AppCompatActivity {
    private EditText wishText;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private String wishID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wish);

        wishText = findViewById(R.id.wishText);

        databaseReference = FirebaseDatabase.getInstance().getReference("wishes");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Wishes wish = snapshot.getValue(Wishes.class);
                    wishText.setText(wish.getWishText());
                    wishID = snapshot.getKey(); // Save the feedback ID for edit and delete operations
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DisplayWish.this, "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit Button Click Listener
        Button buttonEdit = findViewById(R.id.edit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWish();
            }
        });

        // Delete Button Click Listener
        Button buttonDelete = findViewById(R.id.delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWish();
            }
        });
    }

    private void editWish() {
        String newWish = wishText.getText().toString().trim();

        if (!newWish.isEmpty()) {
            Wishes updatedWish = new Wishes(newWish);
            databaseReference.child(wishID).setValue(updatedWish);
            Toast.makeText(this, "Wishes updated.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Fill in ur wishes", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteWish() {
        databaseReference.child(wishID).removeValue();
        wishText.setText("");
        Toast.makeText(this, "Sadly, people would like to read your wish", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DisplayWish.this, MenuSelection.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}