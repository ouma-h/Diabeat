package com.example.diabeat.apiBackend;

import com.example.diabeat.models.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DoctorAPI {
    @GET("doctors/")
    Call<List<Doctor>> getDoctors();

    @GET("doctors/{id}")
    Call<Doctor> getDoctor(@Path("id") int userId);
}
