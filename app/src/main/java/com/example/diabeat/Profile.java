package com.example.diabeat;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.apiBackend.UserApi;
import com.example.diabeat.models.Update;
import com.example.diabeat.models.User;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    EditText lastName;
    EditText firstName;
    EditText birthday;
    EditText emergencyNumber;
    UserApi userApi;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        lastName = findViewById(R.id.lastName);
        firstName = findViewById(R.id.firstName);
        birthday = findViewById(R.id.birthdayInput);
        emergencyNumber = findViewById(R.id.emergencyNumber);
        findViewById(R.id.saveProfile).setOnClickListener(this);
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
        String vemergencyNumber = emergencyNumber.getText().toString().trim();
        if (vemergencyNumber.isEmpty()) {
            emergencyNumber.setError("Emergency Number is required");
            emergencyNumber.requestFocus();
            return;
        }
        if (vemergencyNumber.length() < 8) {
            emergencyNumber.setError("Emergency Number should be atleast 8 character long");
            emergencyNumber.requestFocus();
            return;
        }

        userApi = RetrofitClientInstance.getUserApi();
        updateUser(vlastName, vfirstName, vbirthday, vemergencyNumber);
    }

    public void updateUser(String lastName, String firstName, String birthday, String emergencyNumber){

        Update update = new Update(user.getEmail(), firstName, lastName, birthday, emergencyNumber);

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
