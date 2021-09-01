package com.example.mybudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private EditText Email,Password;
    private Button btnReg;
    private TextView loginHere;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Email = findViewById(R.id.email_reg);
        Password = findViewById(R.id.password_reg);
        btnReg = findViewById(R.id.btn_reg);
        loginHere = findViewById(R.id.login_here);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = Email.getText().toString();
                String passwordString = Password.getText().toString();

                if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)) {
                    Email.setError("Empty Credentials");
                    Password.setError("Empty Credentials");
                } else if (passwordString.length() < 6) {
                    Password.setError("Password short");
                } else {
                    registerUser(emailString,passwordString);
                    progressDialog.setMessage("Processing");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
            }
        });
    }

    private void registerUser(String emailString, String passwordString) {
        mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this,BudgetActivity.class));
                    finish();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(RegistrationActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
}