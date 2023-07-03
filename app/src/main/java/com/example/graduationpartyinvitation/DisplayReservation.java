package com.example.graduationpartyinvitation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayReservation extends AppCompatActivity {

    private EditText editName, editPhone, editNumberAdults, editNumberKid;
    private String reserveId;
    private DatabaseReference dRef;
    private ValueEventListener valueEventListener;
    Button mCancelReserve;
    Button mEditReserve;
    private static final String CHANNEL_ID = "delete_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reservation);

        editName = findViewById(R.id.editTextName);
        editPhone = findViewById(R.id.editTextPhone);
        editNumberAdults = findViewById(R.id.numberAdults);
        editNumberKid = findViewById(R.id.numberKid);

        mCancelReserve = findViewById(R.id.cancel);
        mEditReserve = findViewById(R.id.save);

        dRef = FirebaseDatabase.getInstance().getReference("reserves");

        valueEventListener = dRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReserveForm reserve = snapshot.getValue(ReserveForm.class);

                    // Concatenate the retrieved data and set it to the TextViews
                    editName.setText(reserve.getName());
                    editPhone.setText(reserve.getPhone());
                    editNumberAdults.setText(reserve.getNumberAdult());
                    editNumberKid.setText(reserve.getNumberKids());
                    reserveId = snapshot.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DisplayReservation.this, "Failed to load reservation.", Toast.LENGTH_SHORT).show();
            }
        });

        mEditReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReservation();
            }
        });

        mCancelReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(reserveId);
            }
        });
    }

    private void updateReservation() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String numberAdult = editNumberAdults.getText().toString().trim();
        String numberKids = editNumberKid.getText().toString().trim();

        if (!name.isEmpty() && !phone.isEmpty() && !numberAdult.isEmpty() && !numberKids.isEmpty()) {
            ReserveForm updatedReserve = new ReserveForm(name, phone,numberAdult , numberKids);
            dRef.child(reserveId).setValue(updatedReserve);
            Toast.makeText(this, "Reservation updated successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please fill in all section", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(final String reserveId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to CANCEL the reservation?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteRecord();
            Toast.makeText(this, "Reservation Delete", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteRecord() {
        dRef.child(reserveId).removeValue();
        editName.setText("");
        editPhone.setText("");
        editNumberAdults.setText("");
        editNumberKid.setText("");
        Toast.makeText(this, "Reservation deleted successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DisplayReservation.this, HomePage.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dRef.removeEventListener(valueEventListener);
    }
}