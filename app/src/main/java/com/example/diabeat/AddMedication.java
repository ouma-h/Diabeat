package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMedication extends AppCompatActivity implements View.OnClickListener {
    ProgramAPI apiHolder;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        apiHolder = RetrofitClientInstance.getProgramAPI();
        btnSubmit = findViewById(R.id.addMedBtn);
        btnSubmit.setOnClickListener(this);
    }
    public void submitMed(){
        Medication med = new Medication("Medest",2,"pills",2,"week",true,1);
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

    @Override
    public void onClick(View v) {
        submitMed();
    }
}
