package com.example.diabeat.apiBackend;

import com.example.diabeat.models.ModelProgram;
import com.example.diabeat.models.Medication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProgramAPI {

    @GET("programs")
    Call<List<ModelProgram>> getPrograms(@Query("user_id") int user_id);

    @GET("medications/")
    Call<List<Medication>> getProgramMeds(@Query("prog_id") int prog_id);

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("medications/")
    Call<Medication> createMedication(@Body Medication med);
}
