package com.example.diabeat.apiBackend;

import com.example.diabeat.models.Appointment;
import com.example.diabeat.models.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppointmentAPI {
    @GET("appointements/")
    Call<List<Appointment>> getAppointments(@Query("user_id") int user_id);

}
