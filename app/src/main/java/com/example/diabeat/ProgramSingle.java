package com.example.diabeat;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;
import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.models.ModelProgram;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramSingle extends AppCompatActivity {
    ProgramAPI apiHolder;
    private Integer progID;
    private String prog_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_single);
        prog_name = getIntent().getStringExtra("PROG_NAME");
        apiHolder = RetrofitClientInstance.getProgramAPI();
        progID = getIntent().getIntExtra("PROG_ID",0);
        displayMedications(progID);
        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent npIntent = new Intent(ProgramSingle.this, Programs.class);
                startActivity(npIntent);
                finish();
            }
        });
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

                    ImageView catIm = (ImageView) v.findViewById(R.id.imCategory);
                    switch (med.getCategory()) {
                        case "Inhaled":{
                            catIm.setImageResource(R.drawable.ic_inhealing);
                            break;
                        }
                        case "Pills": {
                            catIm.setImageResource(R.drawable.ic_bottle_pills);
                            break;
                        }
                        case "Lotion": {
                            catIm.setImageResource(R.drawable.ic_cream);
                            break;
                        }
                        case "Syringe": {
                            catIm.setImageResource(R.drawable.ic_injection);
                            break;
                        }
                        default:
                            break;
                    }

                    TextView textView = (TextView) v.findViewById(R.id.programCondition);
                    textView.setText(med.getName());

                    TextView durationView = (TextView) v.findViewById(R.id.textDuration);
                    durationView.setText("For "+med.getDuration()+" day(s).");

                    TextView deleteBtn = v.findViewById(R.id.btnRemove);
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteMedicationPopUp(med.getId(), v);
                        }
                    });

                    v.findViewById(R.id.medCard).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openMedicationResume(med);
                        }
                    });
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
        addmedIntent.putExtra("PROG_ID", progID);
        addmedIntent.putExtra("PROG_NAME", prog_name);
        startActivity(addmedIntent);
        finish();
    }
    private void deleteMedication(final Integer idMed){
        Call<ResponseBody> call = apiHolder.deleteMed(idMed);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Code :"+response.code()+" "+idMed.toString(),Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(),"Deleted! "+idMed.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void deleteMedicationPopUp(final Integer idMed, final View v){
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you really want to delete this medication?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteMedication(idMed);
                        ((View)v.getParent()).setVisibility(View.GONE);
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
    private void openMedicationResume(Medication med) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                ProgramSingle.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.medication_overview,
                (ScrollView) findViewById(R.id.mainView));

        TextView title = bottomSheetView.findViewById(R.id.medName);
        title.setText(med.getName());

        ImageView catIm = (ImageView) bottomSheetView.findViewById(R.id.imCategory);
        switch (med.getCategory()) {
            case "Inhaled":{
                catIm.setImageResource(R.drawable.ic_inhealing);
                break;
            }
            case "Pills": {
                catIm.setImageResource(R.drawable.ic_bottle_pills);
                break;
            }
            case "Lotion": {
                catIm.setImageResource(R.drawable.ic_cream);
                break;
            }
            case "Syringe": {
                catIm.setImageResource(R.drawable.ic_injection);
                break;
            }
            default:
                break;
        }

        TextView amount = bottomSheetView.findViewById(R.id.medAmount);
        amount.setText(med.getAmount()+" unit(s).");

        TextView duration = bottomSheetView.findViewById(R.id.medDuration);
        duration.setText(med.getDuration()+" "+med.getDuration_unit());

        TextView isbefore = bottomSheetView.findViewById(R.id.isBefore);
        if(med.getBefore()){
            isbefore.setText("Before eating.");
        } else {
            isbefore.setText("After eating.");
        }
        String days = "";
        if(med.getMon())
            days += " Mon ";
        if(med.getTue())
            days += " Tue ";
        if(med.getWed())
            days+= " Wed ";
        if(med.getThu())
            days += " Thu ";
        if(med.getFri())
            days += " Fri ";
        if(med.getSat())
            days += " Sat ";
        if(med.getSun())
            days += " Sun ";

        TextView schedule = bottomSheetView.findViewById(R.id.medSchedule);
        schedule.setText(days);

        TextView morning = bottomSheetView.findViewById(R.id.medMorning);
        TextView midday = bottomSheetView.findViewById(R.id.medMidDay);
        TextView night = bottomSheetView.findViewById(R.id.medNight);
        if((med.getMorning() != "00:00:00")) {
            morning.setText("Morning: "+med.getMorning());
        }
        else {
            morning.setText("Morning: None");
        }

        if((med.getMidday() != "00:00:00")) {
            midday.setText("MidDay: "+med.getMidday());
        }
        else {
            midday.setText("MidDay: None");
        }

        if((med.getNight() != "00:00:00")) {
            night.setText("Night: "+med.getNight());
        }
        else {
            night.setText("Night: None");
        }

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}
