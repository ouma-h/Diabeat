package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diabeat.apiBackend.HealthStatusApi;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.apiBackend.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.diabeat.models.User;
import com.google.gson.Gson;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String FILE_NAME = "userId.txt";

    EditText SignUpMail,SignUpPass, SignUpPassRepeat;
    Button SignUpButton;
    UserApi userApi;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUpMail = findViewById(R.id.emailInput);
        SignUpPass = findViewById(R.id.passwordInput);
        SignUpPassRepeat = findViewById(R.id.passwordInput2);
        SignUpButton = findViewById(R.id.spButton);
        SignUpButton.setOnClickListener(this);
        userSignUp();
    }


    private void userSignUp() {
        String email = SignUpMail.getText().toString().trim();
        String password = SignUpPass.getText().toString().trim();
        String RepeatPassword = SignUpPassRepeat.getText().toString().trim();


        if (email.isEmpty()) {
            SignUpMail.setError("Email is required");
            SignUpMail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SignUpMail.setError("Enter a valid email");
            SignUpMail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            SignUpPass.setError("Password should be atleast 6 character long");
            SignUpPass.requestFocus();
            return;
        }

        if (RepeatPassword.length() < 6) {
            SignUpPassRepeat.setError("Password should be atleast 6 character long");
            SignUpPassRepeat.requestFocus();
            return;
        }


        if(!password.equals(RepeatPassword)){
            SignUpPassRepeat.setError("Password don't match. Try again.");
            SignUpPassRepeat.requestFocus();
            return;
        }

        userApi = RetrofitClientInstance.getUserApi();
        signUpUser(email, password);
    }


    public void signUpUser(String email, String password){

        User user = new User(email,password,email);

        Call<User> call = userApi.signUpUser(user);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "code: "+ response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //retrieving the user body
                User userResponse = response.body();
                assert userResponse != null;

                //working with SharedPreferences
                setUserInformation(SignUpActivity.this,userResponse);
                //forward to homepage
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    public void onTextClick (View v){
        Intent myIntent = new Intent(getBaseContext(), activity_login.class);
        startActivity(myIntent);
    }

    @Override
    public void onClick(View v) {
        userSignUp();
    }

    public static void setUserInformation(@NonNull Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("USER", userJson);
        editor.apply();
    }

}
