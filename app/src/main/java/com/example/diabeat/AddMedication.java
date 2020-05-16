package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMedication extends AppCompatActivity implements View.OnClickListener {
    ProgramAPI apiHolder;
    private ImageButton btnCat1;
    private ImageButton btnCat2;
    private ImageButton btnCat3;
    private ImageButton btnCat4;
    private Button btnBefore;
    private Button btnAfter;

    // FORM VARIABLES
    private String name = "MedName";
    private Integer amount = 1;
    private String category = "Pills";
    private Integer duration = 1;
    private String duration_unit = "week";
    private Boolean isBefore = true;
    private Integer prog_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        apiHolder = RetrofitClientInstance.getProgramAPI();

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
        Button btnSubmit = findViewById(R.id.addMedBtn);
        btnSubmit.setOnClickListener(this);
    }
    public void submitMed(){
        EditText medName = (EditText)findViewById(R.id.medName);
        name = medName.getText().toString();
        EditText medAmount = (EditText)findViewById(R.id.medAmount);
        amount = Integer.parseInt(medAmount.getText().toString());
        EditText medDuration = (EditText)findViewById(R.id.medDuration);
        duration = Integer.parseInt(medDuration.getText().toString());

        Medication med = new Medication(name,amount,category,duration,"week",isBefore,1);
        Call<Medication> call = apiHolder.createMedication(med);
        call.enqueue(new Callback<Medication>() {
            @Override
            public void onResponse(Call<Medication> call, Response<Medication> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Code :"+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                Intent spIntent = new Intent(AddMedication.this, ProgramSingle.class);
                spIntent.putExtra("PROG_ID", 1);
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
                category = "Lotion";
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
        }
    }
}