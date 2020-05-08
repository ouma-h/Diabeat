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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_login extends AppCompatActivity {

    private EditText logInEmail, logInPass;
    private FirebaseAuth auth;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        logInEmail = (EditText) findViewById(R.id.logInUserName);
        logInPass = (EditText) findViewById(R.id.logInPassword);
        logInButton = (Button) findViewById(R.id.login_button);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

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

                //authenticate user
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity_login.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 8) {
                                Toast.makeText(getApplicationContext(), "Password must be more than 8 digit", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent(activity_login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

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


