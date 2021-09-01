package com.example.mybudget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private TextView userEmail;
    private Button logoutBtn;
    private ImageView logoutbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutbtn = findViewById(R.id.logoutbtn);
        userEmail = findViewById(R.id.userEmail);


        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("My Budget")
                        .setMessage("Are you sure you want to logout")
                        .setCancelable(false)
                        .setPositiveButton("Yes",(dialog,id) ->{
                          FirebaseAuth.getInstance().signOut();
                          startActivity(new Intent(AccountActivity.this,LoginActivity.class));
                          finish();
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("My Budget")
                        .setMessage("Are you sure you want to logout")
                        .setCancelable(false)
                        .setPositiveButton("Yes",(dialog,id) ->{
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(AccountActivity.this,LoginActivity.class));
                            finish();
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

    }
}