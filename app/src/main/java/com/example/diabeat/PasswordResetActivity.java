package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        inputEmail = findViewById(R.id.userName);
        btnReset = findViewById(R.id.sendEmail);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Please enter your mail address", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });
    }

    public void NavigateLogIn(View v) {
        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
    }
}