package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onTextClick (View v){
        Intent myIntent = new Intent(getBaseContext(), activity_login.class);
        startActivity(myIntent);
    }
}
