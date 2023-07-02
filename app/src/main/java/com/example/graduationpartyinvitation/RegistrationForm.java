package com.example.graduationpartyinvitation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationForm extends AppCompatActivity {
    Button mRegister;
    EditText editEmail, registerPassword, registerPhone, registerFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        mRegister = findViewById(R.id.register);
        editEmail = findViewById(R.id.editEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPhone = findViewById(R.id.registerPhone);
        registerFullName = findViewById(R.id.registerFullName);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerFullName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String phone = registerPhone.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegistrationForm.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {

                    // Register the user with Firebase
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, "password")
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Toast.makeText(RegistrationForm.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegistrationForm.this, LightSensor.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegistrationForm.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}