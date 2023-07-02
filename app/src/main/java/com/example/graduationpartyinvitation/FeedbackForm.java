package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackForm extends AppCompatActivity {
    private EditText editTextType, editTextFeedback;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        editTextType = findViewById(R.id.editTextType);
        editTextFeedback = findViewById(R.id.editTextFeedback);
        Button feedbackSubmit = findViewById(R.id.feedbackSubmit);

        databaseReference = FirebaseDatabase.getInstance().getReference("feedbacks");

        feedbackSubmit.setOnClickListener(v -> addFeedback());
    }

    private void addFeedback() {
        String type = editTextType.getText().toString().trim();
        String feedback = editTextFeedback.getText().toString().trim();

        if (!type.isEmpty() && !feedback.isEmpty()) {
            String feedbackID = databaseReference.push().getKey();

            Feedback feedbackObj = new Feedback(type, feedback);

            databaseReference.child(feedbackID).setValue(feedbackObj);

            // Clear the input fields
            editTextType.setText("");
            editTextFeedback.setText("");

            // Show success toast message
            Toast.makeText(FeedbackForm.this, "Feedback submitted successfully.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FeedbackForm.this, DisplayFeedback.class);
            startActivity(intent);
        }
    }
}