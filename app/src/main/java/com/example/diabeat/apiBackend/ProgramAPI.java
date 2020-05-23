package com.example.diabeat.apiBackend;

import com.example.diabeat.models.ModelProgram;
import com.example.diabeat.models.Medication;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProgramAPI {

    @GET("programs/")
    Call<List<ModelProgram>> getPrograms(@Query("user_id") int user_id);

    @POST("programs/")
    Call<ModelProgram> createProgram(@Body ModelProgram prog);

    @DELETE("programs/{id}/")
    Call<ResponseBody> deleteProgram(@Path("id") Integer id);

    @GET("medications/")
    Call<List<Medication>> getProgramMeds(@Query("prog_id") int prog_id);

    @GET("medications/")
    Call<List<Medication>> getMedsPerUser(@Query("user_id") int user_id);

    @DELETE("medications/{id}/")
    Call<ResponseBody> deleteMed(@Path("id") Integer id);

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("medications/")
    Call<Medication> createMedication(@Body Medication med);
}
