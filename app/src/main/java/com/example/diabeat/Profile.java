package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.apiBackend.UserApi;
import com.example.diabeat.models.Update;
import com.example.diabeat.models.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private final static String FILE_NAME = "userId.txt";

    Integer userId ;
    EditText lastName;
    EditText firstName;
    EditText birthday;
    UserApi userApi;
    User user;

    @SuppressWarnings("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        lastName = findViewById(R.id.lastName);
        firstName = findViewById(R.id.firstName);
        birthday = findViewById(R.id.birthdayInput);
        findViewById(R.id.saveProfile).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        user = MainActivity.getUserInfo(this);

        lastName.setText(user.getLast_name());
        firstName.setText(user.getFirst_name());
        birthday.setText(user.getBirthday());

        //affichage du DatePickerDialog
        birthday = findViewById(R.id.birthdayInput);
        birthday.requestFocus();
        birthday.setOnClickListener(this);

    }


    public void saveEdits(){

        String vlastName = lastName.getText().toString().trim();
        String vfirstName = firstName.getText().toString().trim();
        String vbirthday = birthday.getText().toString().trim();

        userApi = RetrofitClientInstance.getUserApi();
        updateUser(vlastName, vfirstName, vbirthday);
    }

    public void updateUser(String lastName, String firstName, String birthday){

        Update update = new Update(user.getEmail(), firstName, lastName, birthday);

        Call<User> call = userApi.updateUser(user.getId(),update);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Profile.this, "code: "+ response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                SignUpActivity.setUserInformation(Profile.this, response.body());

                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Profile.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" + month + "-" + dayOfMonth;
        birthday.setText(date);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveProfile:
                saveEdits();
                break;
            case R.id.birthdayInput:
                showDatePickerDialog();
                break;
            default:
                break;
        }

    }
}
