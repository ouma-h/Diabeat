package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.diabeat.models.User;

import java.util.Timer;
import java.util.TimerTask;

public class startup_screen extends AppCompatActivity {

    Timer timer;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                user = MainActivity.getUserInfo(startup_screen.this);
                if (user != null ){
                    Intent myIntent= new Intent(startup_screen.this, MainActivity.class);
                    startActivity(myIntent);
                }else{
                    Intent myIntent= new Intent(startup_screen.this, onBoarding.class);
                    startActivity(myIntent);
                }
                finish();


            }
        }, 3000);

    }
}
