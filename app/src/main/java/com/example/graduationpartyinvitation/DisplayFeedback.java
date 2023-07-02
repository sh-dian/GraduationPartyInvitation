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

public class DisplayFeedback extends AppCompatActivity {
    private EditText editTextType, editTextFeedback;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private String feedbackID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_feedback);

        editTextType = findViewById(R.id.displayTextType);
        editTextFeedback = findViewById(R.id.displayTextFeedback);

        databaseReference = FirebaseDatabase.getInstance().getReference("feedbacks");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Feedback feedback = snapshot.getValue(Feedback.class);
                    editTextType.setText(feedback.getType());
                    editTextFeedback.setText(feedback.getFeedback());
                    feedbackID = snapshot.getKey(); // Save the feedback ID for edit and delete operations
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DisplayFeedback.this, "Failed to load feedback.", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit Button Click Listener
        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFeedback();
            }
        });

        // Delete Button Click Listener
        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFeedback();
            }
        });
    }

    private void editFeedback() {
        String newType = editTextType.getText().toString().trim();
        String newFeedback = editTextFeedback.getText().toString().trim();

        if (!newType.isEmpty() && !newFeedback.isEmpty()) {
            Feedback updatedFeedback = new Feedback(newType, newFeedback);
            databaseReference.child(feedbackID).setValue(updatedFeedback);
            Toast.makeText(this, "Feedback updated successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter both Type and Feedback.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFeedback() {
        databaseReference.child(feedbackID).removeValue();
        editTextType.setText("");
        editTextFeedback.setText("");
        Toast.makeText(this, "Feedback deleted successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}