package com.example.diabeat.apiBackend;

import com.example.diabeat.models.Login;
import com.example.diabeat.models.Update;
import com.example.diabeat.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @POST("register/")
    Call<User> signUpUser(@Body User user);

    @POST("login/")
    Call<User> loginUser(@Body Login login);

    @PUT("users/{id}/")
    Call<User> updateUser(@Path("id") int userId, @Body Update update);

}
