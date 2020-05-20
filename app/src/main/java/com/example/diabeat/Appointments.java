package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.apiBackend.AppointmentAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Appointment;
import com.example.diabeat.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Appointments extends AppCompatActivity implements View.OnClickListener {

    User user;
    AppointmentAPI appointmentAPI;
    List<Appointment> appointmentList;
    ViewPager2 appointmentsViewPager;
    TextView nb_appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        findViewById(R.id.back_arrow).setOnClickListener(this);

        user = MainActivity.getUserInfo(this);
        nb_appointments = findViewById(R.id.nb_appointments);
        getAppointments(user.getId());

        appointmentsViewPager = findViewById(R.id.appointmentsViewPager);

        findViewById(R.id.viewMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent spIntent = new Intent(Appointments.this, DoctorsMap.class);
                startActivity(spIntent);
            }
        });

    }


    public void getAppointments(Integer user_id){
        appointmentAPI = RetrofitClientInstance.getAppointmentApi();
        Call<List<Appointment>> call = appointmentAPI.getAppointments(user_id);
        call.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Appointments.this, "code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                appointmentList = response.body();
                nb_appointments.setText(Integer.toString(appointmentList.size()));
                appointmentsViewPager.setAdapter(new AppointmentCardsSlider(appointmentList) );
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(Appointments.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_arrow:
                Intent back = new Intent(this, MainActivity.class);
                startActivity(back);
                break;
            default:
                break;
        }
    }
}
