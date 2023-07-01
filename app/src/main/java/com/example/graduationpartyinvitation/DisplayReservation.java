package com.example.graduationpartyinvitation;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
                    editName.setText("Name: " + reserve.getName());
                    editPhone.setText("Phone: " + reserve.getPhone());
                    editNumberAdults.setText("Number of Adults: " + reserve.getNumberAdult());
                    editNumberKid.setText("Number of Kids: " + reserve.getNumberKids());

                    //delete data
                    mCancelReserve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showConfirmationDialog(snapshot.getKey());
                        }
                    });

                    mEditReserve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateReservation();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finish();
            }
        });
    }

    private void updateReservation() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String numberAdults = editNumberAdults.getText().toString().trim();
        String numberKids = editNumberKid.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
            ReserveForm reserve = new ReserveForm(name, phone, numberAdults, numberKids);
            dRef.child(reserveId).setValue(reserve);
            Toast.makeText(DisplayReservation.this, "Reservation Update", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showConfirmationDialog(final String reserveId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to CANCEL the reservation?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteRecord(reserveId);
            Toast.makeText(this, "Reservation Delete", Toast.LENGTH_SHORT).show();
            clearViews();
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearViews() {
        editName.setText("");
        editPhone.setText("");
        editNumberAdults.setText("");
        editNumberKid.setText("");
    }

    private void deleteRecord(String reserveId) {
        dRef.child(reserveId).removeValue();
        showNotification("Record Deleted");
    }

    private void showNotification(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DeleteNotification";
            String description = "Notification for record deletion";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Toast.makeText(this, "Reservation Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dRef.removeEventListener(valueEventListener);
    }
}