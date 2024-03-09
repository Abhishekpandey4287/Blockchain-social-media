package com.example.social_media_using_blockchain;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextContact, editTextPassword;
    private Button btnRegisterUser;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextContact = findViewById(R.id.editTextContact);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String contact = editTextContact.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !contact.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, contact, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String name, String email, String contact, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            // You can add further actions after successful registration
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
