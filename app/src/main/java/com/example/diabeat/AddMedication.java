package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Calendar.HOUR;

public class AddMedication extends AppCompatActivity implements View.OnClickListener {
    ProgramAPI apiHolder;
    private ImageButton btnCat1;
    private ImageButton btnCat2;
    private ImageButton btnCat3;
    private ImageButton btnCat4;
    private Button btnBefore;
    private Button btnAfter;
    // TIME SELECTOR
    private int mHour;
    private int mMinute;
    private Context ctx = this;
    // PROG ID
    private Integer progID;
    // FORM VARIABLES
    private String name = "MedName";
    private Integer amount = 1;
    private String category = "Pills";
    private Integer duration = 1;
    private String duration_unit = "week";
    private Boolean isBefore = true;
    private String morning = "00:00";
    private String midday = "00:00";
    private String night = "00:00";
    private Boolean isMon = false;
    private Boolean isTue = false;
    private Boolean isWed = false;
    private Boolean isThu = false;
    private Boolean isFri = false;
    private Boolean isSat = false;
    private Boolean isSun = false;
    private Integer prog_id;
    // BTN
    private Button btnSubmit;
    private Button freqMorning;
    private Button freqMidDay;
    private Button freqNight;
    private Button freqBtn;
    // BTN DAYS
    private Button btnMon;
    private Button btnTue;
    private Button btnWed;
    private Button btnThur;
    private Button btnFri;
    private Button btnSat;
    private Button btnSun;
    private Button btnHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        apiHolder = RetrofitClientInstance.getProgramAPI();
        progID = getIntent().getIntExtra("PROG_ID",0);
        // CATEGORY BUTTON
        btnCat1 = findViewById(R.id.btnCat1);
        btnCat1.setOnClickListener(this);
        btnCat2 = findViewById(R.id.btnCat2);
        btnCat2.setOnClickListener(this);
        btnCat3 = findViewById(R.id.btnCat3);
        btnCat3.setOnClickListener(this);
        btnCat4 = findViewById(R.id.btnCat4);
        btnCat4.setOnClickListener(this);

        // BEFORE AFTER
        btnBefore = findViewById(R.id.btnBefore);
        btnBefore.setOnClickListener(this);
        btnAfter = findViewById(R.id.btnAfter);
        btnAfter.setOnClickListener(this);

        // SUBMIT BUTTON
        btnSubmit = findViewById(R.id.addMedBtn);
        btnSubmit.setOnClickListener(this);
        // FREQUENCY BTNS
        freqMorning = (Button)findViewById(R.id.freqMorning);
        freqMorning.setOnClickListener(this);
        freqMidDay = (Button)findViewById(R.id.freqMidDAy);
        freqMidDay.setOnClickListener(this);
        freqNight = (Button)findViewById(R.id.freqNight);
        freqNight.setOnClickListener(this);

        // BTN DAYS
        btnMon = (Button)findViewById(R.id.btnMon);
        btnMon.setOnClickListener(this);
        btnTue = (Button)findViewById(R.id.btnTue);
        btnTue.setOnClickListener(this);
        btnWed = (Button)findViewById(R.id.btnWed);
        btnWed.setOnClickListener(this);
        btnThur = (Button)findViewById(R.id.btnThu);
        btnThur.setOnClickListener(this);
        btnFri = (Button)findViewById(R.id.btnFri);
        btnFri.setOnClickListener(this);
        btnSat = (Button)findViewById(R.id.btnSat);
        btnSat.setOnClickListener(this);
        btnSun = (Button)findViewById(R.id.btnSUn);
        btnSun.setOnClickListener(this);
    }
    public void submitMed(){
        btnSubmit.setEnabled(false);
        EditText medName = (EditText)findViewById(R.id.medName);
        name = medName.getText().toString();
        EditText medAmount = (EditText)findViewById(R.id.medAmount);
        amount = Integer.parseInt(medAmount.getText().toString());
        EditText medDuration = (EditText)findViewById(R.id.medDuration);
        duration = Integer.parseInt(medDuration.getText().toString());

        if(freqMorning.getText().toString().length()>1) {
            morning = freqMorning.getText().toString();
        }
        if(freqMidDay.getText().toString().length()>1) {
            midday = freqMidDay.getText().toString();
        }
        if(freqNight.getText().toString().length()>1) {
            night = freqNight.getText().toString();
        }

        Medication med = new Medication(name,amount,category,duration,"week", isMon, isTue, isWed, isThu, isFri, isSat, isSun, morning, midday, night,isBefore, progID, MainActivity.getUserInfo(this).getId());
        Call<Medication> call = apiHolder.createMedication(med);
        call.enqueue(new Callback<Medication>() {
            @Override
            public void onResponse(Call<Medication> call, Response<Medication> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Code :"+response.code(),Toast.LENGTH_LONG).show();
                    btnSubmit.setEnabled(true);
                    return;
                }
                Calendar c = Calendar.getInstance();
                setRedminder(response.body());
                Intent spIntent = new Intent(AddMedication.this, ProgramSingle.class);
                spIntent.putExtra("PROG_ID", progID);
                startActivity(spIntent);
            }

            @Override
            public void onFailure(Call<Medication> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showToast(String toastMsg){
        Toast.makeText(getApplicationContext(),toastMsg,Toast.LENGTH_LONG).show();
    }
    private void unselectBtnsCat(){
        btnCat1.setBackgroundResource(R.drawable.round_button_white);
        btnCat2.setBackgroundResource(R.drawable.round_button_white);
        btnCat3.setBackgroundResource(R.drawable.round_button_white);
        btnCat4.setBackgroundResource(R.drawable.round_button_white);
    }
    private void unselectBeforeAfter(){
        btnBefore.setBackgroundResource(R.drawable.round_button_white);
        btnAfter.setBackgroundResource(R.drawable.round_button_white);
    }
    private void show_Timepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {

                        mHour = pHour;
                        mMinute = pMinute;
                        freqBtn.setBackgroundResource(R.drawable.round_button);
                        freqBtn.setText(String.format("%02d" , mHour)+":"+String.format("%02d" , mMinute));

                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
    }
    private void selectFrequency(View v){
        freqBtn = (Button)v;
        if(freqBtn.getText().toString().length()>0) {
            freqBtn.setBackgroundResource(R.drawable.round_button_white);
            freqBtn.setText("");
        } else {
            show_Timepicker();
        }
    }
    private void toggleDay(View v){
        btnHolder = (Button)v;
        if(v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.round_button_white).getConstantState())) {
            v.setBackgroundResource(R.drawable.round_button);
        } else {
            v.setBackgroundResource(R.drawable.round_button_white);
        }

        switch (btnHolder.getText().toString()){
            case "Sun":
                isSun = !isSun;
                break;
            case "Mon":
                isMon = !isMon;
                break;
            case "Tue":
                isTue = !isTue;
                break;
            case "Wed":
                isWed = !isWed;
                break;
            case "Thu":
                isThu = !isThu;
                break;
            case "Fri":
                isFri = !isFri;
                break;
            case "Sat":
                isSat = !isSat;
                break;
        }
    }
    private void setRedminder(Medication med){
        Calendar daterem = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Gson gson = new Gson();
        daterem.set(Calendar.HOUR_OF_DAY, mHour);
        daterem.set(Calendar.MINUTE, mMinute);
        daterem.set(Calendar.SECOND, 0);
        long timenow = System.currentTimeMillis();
        long padding = 1000*5;
        AlarmManager medReminder = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("Title", "Diabete: "+getIntent().getStringExtra("PROG_NAME"));
        intent.putExtra("Content", "It's time to take your "+df.format(daterem.getTime())+" "+med.getName()+". You need "+med.getAmount()+" unit(s).");
        intent.putExtra("med", gson.toJson(med));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);



        //medReminder.setExact(AlarmManager.RTC_WAKEUP, daterem.getTimeInMillis(), pendingIntent);
        medReminder.setExact(AlarmManager.RTC_WAKEUP, timenow+padding, pendingIntent);

        Log.i("REMINDER","Reminder created");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // SUBMIT FORM
            case  R.id.addMedBtn: {
                submitMed();
                break;
            }

            // SELECT CATEGORY
            case R.id.btnCat1: {
                showToast("Cat 1 selected!");
                unselectBtnsCat();
                v.setBackgroundResource(R.drawable.round_button);
                category = "Inhaled";
                break;
            }
            case R.id.btnCat2: {
                showToast("Cat 2 selected!");
                unselectBtnsCat();
                v.setBackgroundResource(R.drawable.round_button);
                category = "Pills";
                break;
            }
            case R.id.btnCat3: {
                showToast("Cat 3 selected!");
                unselectBtnsCat();
                v.setBackgroundResource(R.drawable.round_button);
                category = "Ointment";
                break;
            }
            case R.id.btnCat4: {
                showToast("Cat 4 selected!");
                unselectBtnsCat();
                v.setBackgroundResource(R.drawable.round_button);
                category = "Syringe";
                break;
            }

            // BEFORE / AFTER
            case R.id.btnBefore: {
                showToast("Before selected!");
                unselectBeforeAfter();
                v.setBackgroundResource(R.drawable.round_button);
                isBefore = true;
                break;
            }
            case R.id.btnAfter: {
                showToast("After selected!");
                unselectBeforeAfter();
                v.setBackgroundResource(R.drawable.round_button);
                isBefore = false;
                break;
            }
            // DAYS
            case R.id.btnSUn:
            case R.id.btnMon:
            case R.id.btnTue:
            case R.id.btnWed:
            case R.id.btnThu:
            case R.id.btnFri:
            case R.id.btnSat:
                toggleDay(v);
                break;
            // FREQUENCY
            case R.id.freqMorning:
                selectFrequency(v);
                break;
            case R.id.freqMidDAy:
                selectFrequency(v);
                break;
            case R.id.freqNight:
                selectFrequency(v);
                break;
        }
    }
}