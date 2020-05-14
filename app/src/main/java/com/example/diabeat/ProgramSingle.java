package com.example.diabeat;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;
import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.models.ModelProgram;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramSingle extends AppCompatActivity {
    ProgramAPI apiHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_single);
        apiHolder = RetrofitClientInstance.getProgramAPI();
        displayMedications(getIntent().getIntExtra("PROG_ID",0));
    }
    public void displayMedications(Integer progID){
        Call<List<Medication>> call = apiHolder.getProgramMeds(progID);

        call.enqueue(new Callback<List<Medication>>() {

            @Override
            public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
                List<Medication> meds = response.body();
                for(final Medication med : meds) {
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.medication_card, null, false);

                    TextView textView = (TextView) v.findViewById(R.id.programCondition);
                    textView.setText(med.getName());

                    TextView durationView = (TextView) v.findViewById(R.id.textDuration);
                    durationView.setText(med.getDuration()+" "+med.getDuration_unit());

                    ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearCards);
                    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                }
            }

            @Override
            public void onFailure(Call<List<Medication>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void newMed(View v){
        Intent addmedIntent = new Intent(ProgramSingle.this, AddMedication.class);
        startActivity(addmedIntent);
    }
}
