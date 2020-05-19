package com.example.diabeat.apiBackend;

import com.example.diabeat.models.BloodPressure;
import com.example.diabeat.models.Temperature;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HealthStatusApi {

    @POST("temperatures/")
    Call<Temperature> addTemperature(@Body Temperature temperature);

    @POST("bloodpressure/")
    Call<BloodPressure> addBloodPressure(@Body BloodPressure bloodPressure);

    @GET("temperatures/")
    Call<List<Temperature>> getTemperatures(@Query("user_id") int user_id);

    @GET("bloodpressure/")
    Call<List<BloodPressure>> getBloodPressures(@Query("user_id") int user_id);


}
