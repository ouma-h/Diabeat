package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class SignUpActivity extends AppCompatActivity {

    EditText SignUpMail,SignUpPass;
    Button SignUpButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUpMail = findViewById(R.id.emailInput);
        SignUpPass = findViewById(R.id.passwordInput);
        SignUpButton = (Button) findViewById(R.id.spButton);
    }
    public void onTextClick (View v){
        Intent myIntent = new Intent(getBaseContext(), activity_login.class);
        startActivity(myIntent);
    }
}
