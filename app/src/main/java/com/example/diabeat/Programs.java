package com.example.diabeat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.models.ModelProgram;
import com.example.diabeat.apiBackend.ProgramAPI;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.User;

public class Programs extends AppCompatActivity {
    ProgramAPI apiHolder;
    int userID;
    private Button btnNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        apiHolder = RetrofitClientInstance.getProgramAPI();
        userID = MainActivity.getUserInfo(this).getId();
        displayPrograms();
        btnNew = (Button)findViewById(R.id.newProg);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent npIntent = new Intent(Programs.this, AddProgram.class);
                startActivity(npIntent);
            }
        });
        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent npIntent = new Intent(Programs.this, MainActivity.class);
                startActivity(npIntent);
                finish();
            }
        });

    }
    public void displayPrograms(){



        Call<List<ModelProgram>> call = apiHolder.getPrograms(userID);

        call.enqueue(new Callback<List<ModelProgram>>() {

            @Override
            public void onResponse(Call<List<ModelProgram>> call, Response<List<ModelProgram>> response) {
                List<ModelProgram> programs = response.body();
                for(final ModelProgram program : programs) {
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.program_card, null, false);

                    // Program title
                    TextView titleView = (TextView)v.findViewById(R.id.programCondition);
                    titleView.setText(program.getCondition());

                    // Program duration
                    TextView durationView = (TextView)v.findViewById(R.id.textDuration);
                    durationView.setText(program.getDuration()+" weeks");

                    // Button event listener
                    CardView card = (CardView) v.findViewById(R.id.wrapper);
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent spIntent = new Intent(Programs.this, ProgramSingle.class);
                            spIntent.putExtra("PROG_ID", program.getId());
                            spIntent.putExtra("PROG_NAME", program.getCondition());
                            startActivity(spIntent);
                        }
                    });

                    v.findViewById(R.id.btnRemove).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePrgoramPopUp(program.getId(), v);
                        }
                    });
                    ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearCards);
                    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                }
            }

            @Override
            public void onFailure(Call<List<ModelProgram>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteProgram(final Integer idProg){
        Call<ResponseBody> call = apiHolder.deleteProgram(idProg);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Code :"+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(),"Deleted! "+idProg.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void deletePrgoramPopUp(final Integer idProgram, final View v){
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you really want to delete this program?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteProgram(idProgram);
                        ((View)v.getParent()).setVisibility(View.GONE);
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
}
