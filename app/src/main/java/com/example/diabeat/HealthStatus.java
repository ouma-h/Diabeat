package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.apiBackend.HealthStatusApi;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.BloodPressure;
import com.example.diabeat.models.Temperature;
import com.example.diabeat.models.User;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthStatus extends AppCompatActivity implements View.OnClickListener{

    HealthStatusApi healthStatusApi;
    TextView temperature;
    SimpleDateFormat display;
    SimpleDateFormat apiFormat;
    TextView diastolic;
    TextView systolic;
    TextView temp_date;
    TextView bp_date;
    User user;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_status);

        healthStatusApi = RetrofitClientInstance.getHealthStatusApi();

        display = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
        apiFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        findViewById(R.id.back_arrow).setOnClickListener(this);
        findViewById(R.id.addTemperature).setOnClickListener(this);
        findViewById(R.id.addBloodPressure).setOnClickListener(this);

        user = MainActivity.getUserInfo(this);
        temperature = findViewById(R.id.temp_measure);
        diastolic = findViewById(R.id.diastolic);
        systolic = findViewById(R.id.systolic);
        temp_date = findViewById(R.id.temp_date);
        bp_date = findViewById(R.id.bp_date);


    }

    public void addTemperature(String temp, String temp_date){


        Temperature temperature = new Temperature(user.getId(),temp, temp_date);
        Call<Temperature> call = healthStatusApi.addTemperature(temperature);
        call.enqueue(new Callback<Temperature>() {
            @Override
            public void onResponse(Call<Temperature> call, Response<Temperature> response) {
                Toast.makeText(HealthStatus.this, "code: "+ response.code(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Temperature> call, Throwable t) {
                Toast.makeText(HealthStatus.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    public void addBloodPressure(String sys, String dias,  String bloodPressure_date){

        BloodPressure bloodPressure = new BloodPressure(user.getId(),dias, sys, bloodPressure_date);

        Call<BloodPressure> call = healthStatusApi.addBloodPressure(bloodPressure);
        call.enqueue(new Callback<BloodPressure>() {
            @Override
            public void onResponse(Call<BloodPressure> call, Response<BloodPressure> response) {
                Toast.makeText(HealthStatus.this, "code: "+ response.code(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BloodPressure> call, Throwable t) {
                Toast.makeText(HealthStatus.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getTemperatures(Integer user_id){
        Call<List<Temperature>> call = healthStatusApi.getTemperatures(user_id);

        call.enqueue(new Callback<List<Temperature>>() {
            @Override
            public void onResponse(Call<List<Temperature>> call, Response<List<Temperature>> response) {
                Toast.makeText(HealthStatus.this, "code: "+ response.code(),
                        Toast.LENGTH_LONG).show();
            }



            @Override
            public void onFailure(Call<List<Temperature>> call, Throwable t) {

            }
        });

    }


    public void openTempDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                HealthStatus.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.add_temperature_dialog,
                (LinearLayout)findViewById(R.id.temp_bottom_sheet));

        bottomSheetView.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp = bottomSheetView.findViewById(R.id.temp);
                String tp = temp.getText().toString();
                temperature.setText(tp);
                temp_date.setText(currentDateTime(display));
                bottomSheetDialog.dismiss();
                addTemperature(tp, currentDateTime(apiFormat));
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();


    }

    public void openBPDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                HealthStatus.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.add_blood_pressure,
                (LinearLayout)findViewById(R.id.blood_pressure_bottom_sheet));

        bottomSheetView.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.plusBP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText vdiastolic = bottomSheetView.findViewById(R.id.diastolic);
                EditText vsystolic = bottomSheetView.findViewById(R.id.systolic);
                String d = vdiastolic.getText().toString();
                String s = vsystolic.getText().toString();
                diastolic.setText(d);
                systolic.setText(s);
                bp_date.setText(currentDateTime(display));
                bottomSheetDialog.dismiss();
                addBloodPressure(s,d, currentDateTime(apiFormat));
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }


//    \*************************************** TEMPERATURE CHART *****************************************************\


    private ArrayList<Entry> TemperatureValues(){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();

        //mapping the temperatures' list

        return dataVals;

    }










    public String currentDateTime(SimpleDateFormat s){
        Calendar calendar = Calendar.getInstance();
        return (s.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case  R.id.back_arrow:
                Intent intent1 = new Intent(this , MainActivity.class);
                startActivity(intent1);
                break;
            case  R.id.addTemperature:
                openTempDialog();
                break;
            case  R.id.addBloodPressure:
                openBPDialog();
                break;

        }
    }
}
