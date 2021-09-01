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

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private Button btnLogin;
    private TextView signUp;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();


        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.signup_reg);


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)) {
                    email.setError("Empty Credentials");
                    password.setError("Empty Credentials");
                } else if (passwordString.length() < 6) {
                    password.setError("Password short");
                } else {

                    loginUser(emailString,passwordString);
                    progressDialog.setMessage("Processing");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
            }
        });
    }

    private void loginUser(String emailString, String passwordString) {
        mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,BudgetActivity.class));
                    finish();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }
}
