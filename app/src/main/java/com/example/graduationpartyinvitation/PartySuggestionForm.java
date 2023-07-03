package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PartySuggestionForm extends AppCompatActivity {
    private EditText suggestActivity, description;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_suggestion_form);

        suggestActivity = findViewById(R.id.suggestActivity);
        description = findViewById(R.id.description);
        Button submit = findViewById(R.id.submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("suggestions");

        submit.setOnClickListener(v -> addSuggestion());
    }

    private void addSuggestion() {
        String name = suggestActivity.getText().toString().trim();
        String desc = description.getText().toString().trim();

        if (!desc.isEmpty() && !name.isEmpty()) {
            String suggestionID = databaseReference.push().getKey();

            SuggestionActivity suggestionObj = new SuggestionActivity(name, desc);

            databaseReference.child(suggestionID).setValue(suggestionObj);

            // Clear the input fields
            suggestActivity.setText("");
            description.setText("");

            // Show success toast message
            Toast.makeText(PartySuggestionForm.this, "Great Suggestion!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PartySuggestionForm.this, displayActivity.class);
            startActivity(intent);
        }
    }
}