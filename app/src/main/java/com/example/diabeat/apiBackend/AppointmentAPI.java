package com.example.diabeat.apiBackend;

import com.example.diabeat.models.Appointment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppointmentAPI {
    @GET("appointements/")
    Call<List<Appointment>> getAppointments(@Query("user") int user);

    @POST("appointements/")
    Call<Appointment> createAppointment(@Body Appointment app);

}
