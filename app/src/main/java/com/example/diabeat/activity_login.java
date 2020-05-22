package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.apiBackend.UserApi;
import com.example.diabeat.models.Login;
import com.example.diabeat.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_login extends AppCompatActivity implements View.OnClickListener {

    private EditText logInEmail, logInPass;
    private Button logInButton;
    UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInEmail = findViewById(R.id.logInUserName);
        logInPass = findViewById(R.id.logInPassword);
        logInButton = findViewById(R.id.login_button);
        logInButton.setOnClickListener(this);

    }

    public void userLogin(){
        String email = logInEmail.getText().toString();
        final String password = logInPass.getText().toString();

        if (email.isEmpty()) {
            logInEmail.setError("Email is required");
            logInEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            logInEmail.setError("Enter a valid email");
            logInEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            logInPass.setError("Password required");
            logInPass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            logInPass.setError("Password should be atleast 6 character long");
            logInPass.requestFocus();
            return;
        }

        userApi = RetrofitClientInstance.getUserApi();
        loginUser(email, password);
    }

    public void loginUser(String email, String password){
        final Login login = new Login(email, password);
        Call<User> call = userApi.loginUser(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity_login.this, "Try Again",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                User userResponse = response.body();

                assert userResponse != null;
                SignUpActivity.setUserInformation(activity_login.this, userResponse);
                Intent myIntent = new Intent(activity_login.this, MainActivity.class);
                startActivity(myIntent);
            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(activity_login.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void navigateSignUp(View v) {
        Intent myIntent = new Intent(activity_login.this, SignUpActivity.class);
        startActivity(myIntent);
    }

    public void NavigateForgetMyPassword(View v) {
        Intent prIntent = new Intent(activity_login.this, PasswordResetActivity.class);
        startActivity(prIntent);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(activity_login.this, "Please wait!",
                Toast.LENGTH_LONG).show();
        userLogin();
    }
}


