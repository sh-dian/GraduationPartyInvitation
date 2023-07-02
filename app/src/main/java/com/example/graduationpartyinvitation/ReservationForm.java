package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReservationForm extends AppCompatActivity {

    private EditText editTextName, editTextPhone,numberAdults,numberKid;

    private DatabaseReference databaseReference;
    Button nfcSensor, mReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_form);

        editTextName = findViewById(R.id.editFullName);
        editTextPhone = findViewById(R.id.editTextPhone);
        numberAdults = findViewById(R.id.editAdultNumber);
        numberKid = findViewById(R.id.editKidNumber);

        mReserve = findViewById(R.id.submit);
        nfcSensor = findViewById(R.id.nfc);

        nfcSensor.setOnClickListener(v -> {
            Intent intent = new Intent(ReservationForm.this, NFCSensor.class);
            startActivity(intent);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("reserves");

        mReserve.setOnClickListener(v -> reserve());
    }

    private void reserve() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String numberAdult = numberAdults.getText().toString().trim();
        String numberKids = numberKid.getText().toString().trim();

        if (!name.isEmpty() && !phone.isEmpty()) {
            String reserveId = databaseReference.push().getKey();

            ReserveForm reserve = new ReserveForm(name, phone, numberAdult, numberKids);

            databaseReference.child(reserveId).setValue(reserve);

            // Clear the input fields
            editTextName.setText("");
            editTextPhone.setText("");
            numberAdults.setText("");
            numberKid.setText("");

            Intent intent = new Intent(ReservationForm.this,FingerPrintSensor.class);
            Toast.makeText(this, "See you at the party!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
}