package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diabeat.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_PHONE_CALL = 0;
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
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.emergency).setOnClickListener(this);

        user = getUserInfo(this);
        if (user.getFirst_name().length()>1) {
            userFirstName.setText("Hi, " + user.getFirst_name() + "!");

        }

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
/*
        String currentTime = DateFormat.getTimeInstance().format(calendar.getTime());
*/
        TextView textViewDate = findViewById(R.id.currentDate);
        textViewDate.setText(currentDate);


        findViewById(R.id.prescriptions_card).setOnClickListener(this);
        findViewById(R.id.appointment_card).setOnClickListener(this);
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

    public void logout(@NonNull Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USER");
        editor.apply();
        Intent myIntent = new Intent(getBaseContext(), activity_login.class);
        startActivity(myIntent);
    }

    public void openEmergencyDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.emergency,
                (LinearLayout) findViewById(R.id.emergency_bottom_sheet));
        ImageButton btnCallAmb = bottomSheetView.findViewById(R.id.btnCallAmb);
        ImageButton btnCallPolice = bottomSheetView.findViewById(R.id.btnCallPolice);
        ImageButton btnCallSAMU = bottomSheetView.findViewById(R.id.btnCallSAMU);

        btnCallAmb.setImageResource(R.drawable.ic_telephone);
        btnCallPolice.setImageResource(R.drawable.ic_telephone);
        btnCallSAMU.setImageResource(R.drawable.ic_telephone);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetView.findViewById(R.id.btnCallSAMU).setOnClickListener(this);
        bottomSheetView.findViewById(R.id.btnCallPolice).setOnClickListener(this);
        bottomSheetView.findViewById(R.id.btnCallAmb).setOnClickListener(this);
        bottomSheetDialog.show();

    }

    private void makeCall(String phoneNum) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNum));
            startActivity(callIntent);
        }

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
            case R.id.prescriptions_card:
                Intent ipcard = new Intent(this, Programs.class);
                this.startActivity(ipcard);
                break;
            case R.id.logout:
                logout(this);
                break;
            case R.id.appointment_card:
                Intent appCard = new Intent(this, Appointments.class);
                startActivity(appCard);
                break;
            case R.id.emergency:
                openEmergencyDialog();
                break;
            case R.id.btnCallAmb:
                makeCall("71725555");
                break;
            case R.id.btnCallPolice:
                makeCall("197");
                break;
            case R.id.btnCallSAMU:
                makeCall("190");
                break;
            default:
                break;
        }
    }
}
