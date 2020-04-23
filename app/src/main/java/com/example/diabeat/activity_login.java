package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onTextClick (View v){
        Intent myIntent = new Intent(activity_login.this, SignUpActivity.class);
        startActivity(myIntent);
    }
}
