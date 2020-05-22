package com.example.diabeat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;
import com.example.diabeat.models.ModelProgram;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProgram extends AppCompatActivity {
    // API HOLDER
    ProgramAPI apiHolder;
    // PROGRAM INSTANCE ATTRIBUTES
    private String condition;
    private Integer duration;
    private  String duration_unit;
    private  String start_date;
    private Integer user_id;
    // DATE TIME PICKER CODE SNIPPET
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Calendar c;
    private Context ctx = this;
    private  TextView porgStartDate;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);
        // PROGRAM API LOADER
        apiHolder = RetrofitClientInstance.getProgramAPI();

        // DATE PICKER
        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);
        findViewById(R.id.progStartDate).requestFocus();
        porgStartDate =findViewById(R.id.progStartDate);
        porgStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Datepicker();
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProgram();
            }
        });

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent npIntent = new Intent(AddProgram.this, Programs.class);
                startActivity(npIntent);
                finish();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void show_Datepicker() {
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth-1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + monthOfYear + "-" + dayOfMonth;
                        porgStartDate.setText(date);
                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
    }
    public void submitProgram(){
        EditText proCondition = findViewById(R.id.progCondition);
        EditText progDuration = findViewById(R.id.progDuration);
        EditText progDurUnit = findViewById(R.id.progDurUnit);
        EditText progStartDate = findViewById(R.id.progStartDate);

        condition = proCondition.getText().toString();
        duration = Integer.parseInt(progDuration.getText().toString());
        duration_unit = progDurUnit.getText().toString();
        start_date = progStartDate.getText().toString();
        user_id = MainActivity.getUserInfo(this).getId();

        ModelProgram prog = new ModelProgram(condition, duration, duration_unit, start_date, user_id);

        Call<ModelProgram> call = apiHolder.createProgram(prog);
        call.enqueue(new Callback<ModelProgram>() {
            @Override
            public void onResponse(Call<ModelProgram> call, Response<ModelProgram> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Code :"+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                Intent spIntent = new Intent(AddProgram.this, Programs.class);
                startActivity(spIntent);
            }

            @Override
            public void onFailure(Call<ModelProgram> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
