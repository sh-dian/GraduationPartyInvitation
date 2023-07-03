package com.example.graduationpartyinvitation;

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

public class displayActivity extends AppCompatActivity {
    private EditText suggestActivity, description;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private String suggestionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        suggestActivity = findViewById(R.id.suggestActivity);
        description = findViewById(R.id.description);
        databaseReference = FirebaseDatabase.getInstance().getReference("suggestions");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SuggestionActivity suggestionActivity = snapshot.getValue(SuggestionActivity.class);
                    suggestActivity.setText(suggestionActivity.getName());
                    description.setText(suggestionActivity.getDescription());
                    suggestionID = snapshot.getKey(); // Save the feedback ID for edit and delete operations
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(displayActivity.this, "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit Button Click Listener
        Button buttonEdit = findViewById(R.id.edit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSuggestion();
            }
        });

        // Delete Button Click Listener
        Button buttonDelete = findViewById(R.id.delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSuggestion();
            }
        });
    }

    private void editSuggestion() {
        String name = suggestActivity.getText().toString().trim();
        String desc = description.getText().toString().trim();

        if (!desc.isEmpty() && !name.isEmpty()) {
            SuggestionActivity update = new SuggestionActivity(desc,name);
            databaseReference.child(suggestionID).setValue(update);
            Toast.makeText(this, "Suggestion updated.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Fill in ur Suggestion", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSuggestion() {
        databaseReference.child(suggestionID).removeValue();
        suggestActivity.setText("");
        description.setText("");
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}