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

public class activity_login extends AppCompatActivity {

    private EditText logInEmail, logInPass;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInEmail = (EditText) findViewById(R.id.logInUserName);
        logInPass = (EditText) findViewById(R.id.logInPassword);
        logInButton = (Button) findViewById(R.id.login_button);

        logInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = logInEmail.getText().toString();
                final String password = logInPass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your mail address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    public void navigateSignUp(View v) {
        Intent myIntent = new Intent(activity_login.this, SignUpActivity.class);
        startActivity(myIntent);
    }

    public void NavigateForgetMyPassword(View v) {
        Intent prIntent = new Intent(activity_login.this, PasswordResetActivity.class);
        startActivity(prIntent);
    }
}


