package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;
import com.example.diabeat.models.ModelProgram;
import com.example.diabeat.models.Doctor;
import com.example.diabeat.models.Medication;
import com.example.diabeat.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_PHONE_CALL = 0;
    private TextView userFirstName;
    ViewPager2 medicationsViewPager;
    User user;
    private String medObject;
    ProgramAPI programAPI;
    Calendar calendar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userFirstName = findViewById(R.id.userFirstName);


        user = getUserInfo(this);
        if (user.getFirst_name().length()>2) {
            userFirstName.setText("Hi, " + user.getFirst_name() + "!");
        }else{
            userFirstName.setText("Hi, Guest!");
        }
        getMedications(user.getId());

        medicationsViewPager = findViewById(R.id.medicationsViewPager);

        medicationsViewPager.setClipToPadding(false);
        medicationsViewPager.setClipChildren(false);
        medicationsViewPager.setOffscreenPageLimit(3);
        medicationsViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r* 0.05f);
            }
        });

        medicationsViewPager.setPageTransformer(compositePageTransformer);

        findViewById(R.id.profile).setOnClickListener(this);
        findViewById(R.id.dailyHealth_card).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.emergency).setOnClickListener(this);
        findViewById(R.id.history_card).setOnClickListener(this);

        user = getUserInfo(this);
        if (user.getFirst_name().length()>1) {
            userFirstName.setText("Hi, " + user.getFirst_name() + "!");

        }
        findViewById(R.id.prescriptions_card).setOnClickListener(this);
        findViewById(R.id.appointment_card).setOnClickListener(this);

        calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.currentDate);
        textViewDate.setText(currentDate);

        findViewById(R.id.prescriptions_card).setOnClickListener(this);
        findViewById(R.id.appointment_card).setOnClickListener(this);

        checkNotification();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if(!user.getFirst_name().equals(userFirstName)){
            userFirstName.setText("Hi, " + user.getFirst_name() + "!");
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
        editor.remove("HISTORY");
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
    public void openConfirmDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.confirm_skip,
                (LinearLayout) findViewById(R.id.confirmSkip));

        bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHistory(false);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.btnSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHistory(true);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
    private void checkNotification(){
        NotificationManager notifyMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            boolean cameFromNotification = b.getBoolean("fromNotification");
            if(cameFromNotification) {
                medObject = b.getString("med");
                openConfirmDialog();
                notifyMgr.cancel(1);
            }
        }
    }
    private void updateHistory(Boolean isDiscarded){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String hist = sharedPreferences.getString("HISTORY", "");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("HISTORY", "");
        Gson gson = new Gson();
        Medication med = gson.fromJson(medObject, Medication.class);
        med.setIs_discarded(isDiscarded);
        medObject = gson.toJson(med);
        if(hist.length()>0){
            editor.putString("HISTORY", hist+","+medObject);
        } else {
            editor.putString("HISTORY", medObject);
        }

        editor.apply();
    }

    public void getMedications(Integer user_id){

        programAPI = RetrofitClientInstance.getProgramAPI();
        Call<List<Medication>> call = programAPI.getMedsPerUser(user_id);
        call.enqueue(new Callback<List<Medication>>() {
            @Override
            public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
                List<Medication> medsPerDay = new ArrayList<>();
                assert response.body() != null;
                for(final Medication medication : response.body()) {
                    switch (calendar.get(Calendar.DAY_OF_WEEK)){
                        case 1 :
                            if (medication.getSun()){
                                medsPerDay.add(medication);
                            }
                            break;
                        case 2 :
                            if (medication.getMon()){
                                medsPerDay.add(medication);
                            }
                            break;
                        case 3 :
                            if (medication.getTue()){
                                medsPerDay.add(medication);
                            }
                            break;
                        case 4 :
                            if (medication.getWed()){
                                medsPerDay.add(medication);
                            }
                            break;
                        case 5 :
                            if (medication.getThu()){
                                medsPerDay.add(medication);
                            }
                            break;
                        case 6 :
                            if (medication.getFri()){
                                medsPerDay.add(medication);
                            }
                            break;
                        case 7 :
                            if (medication.getSat()){
                                medsPerDay.add(medication);
                            }
                            break;
                    }

                }
                medicationsViewPager.setAdapter(new MedicationCardsSlider(medsPerDay) );
            }

            @Override
            public void onFailure(Call<List<Medication>> call, Throwable t) {

            }
        });
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
            case R.id.history_card:
                Intent histCard = new Intent(this, History.class);
                startActivity(histCard);
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
