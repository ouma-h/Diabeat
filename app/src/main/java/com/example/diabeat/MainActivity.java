package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diabeat.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView userFirstName;
    User user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userFirstName = findViewById(R.id.userFirstName);

        findViewById(R.id.profile).setOnClickListener(this);
        findViewById(R.id.dailyHealth_card).setOnClickListener(this);
        user = getUserInfo(this);
        if (!user.getFirst_name().equals("")) {
            userFirstName.setText("Hi, " + user.getFirst_name() + "!");

        }

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
/*
        String currentTime = DateFormat.getTimeInstance().format(calendar.getTime());
*/
        TextView textViewDate = findViewById(R.id.currentDate);
        textViewDate.setText(currentDate);



    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if(!user.getFirst_name().equals(userFirstName)){
            userFirstName.setText("Hi, " + user.getFirst_name() + "!");
        }else if (user.getFirst_name().equals("")){
            userFirstName.setText(R.string.hi_guest);
        }
    }

    public static User getUserInfo(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("USER", "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.profile:
                Intent intent1 = new Intent(this, Profile.class);
                this.startActivity(intent1);
                break;
            case R.id.dailyHealth_card:
                Intent intent2 = new Intent(this, HealthStatus.class);
                this.startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
