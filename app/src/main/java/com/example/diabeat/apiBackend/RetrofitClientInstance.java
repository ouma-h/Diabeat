package com.example.diabeat.apiBackend;

import com.example.diabeat.HealthStatus;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://diabeatbackend.herokuapp.com/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static ProgramAPI getProgramAPI() {
        Retrofit instance = RetrofitClientInstance.getRetrofitInstance();
        ProgramAPI apiHolder = instance.create(ProgramAPI.class);
        return apiHolder;
    }

    public static UserApi getUserApi() {
        Retrofit mInstance = RetrofitClientInstance.getRetrofitInstance();
        UserApi userApi=  mInstance.create(UserApi.class);
        return userApi;
    }

    public static HealthStatusApi getHealthStatusApi() {
        Retrofit mInstance = RetrofitClientInstance.getRetrofitInstance();
        HealthStatusApi healthStatusApi=  mInstance.create(HealthStatusApi.class);
        return healthStatusApi;
    }
    public static DoctorAPI getDoctorApi() {
        Retrofit mInstance = RetrofitClientInstance.getRetrofitInstance();
        DoctorAPI doctorAPIholer = mInstance.create(DoctorAPI.class);
        return doctorAPIholer;
    }
    public static AppointmentAPI getAppointmentApi() {
        Retrofit mInstance = RetrofitClientInstance.getRetrofitInstance();
        AppointmentAPI appointmentAPIholder = mInstance.create(AppointmentAPI.class);
        return appointmentAPIholder;
    }
}
