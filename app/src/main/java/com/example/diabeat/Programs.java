package com.example.diabeat;

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

import com.example.diabeat.models.ModelProgram;
import com.example.diabeat.apiBackend.ProgramAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.User;

public class Programs extends AppCompatActivity {
    ProgramAPI apiHolder;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        apiHolder = RetrofitClientInstance.getProgramAPI();
        user = MainActivity.getUserInfo(this);
        displayPrograms();
    }
    public void displayPrograms(){



        Call<List<ModelProgram>> call = apiHolder.getPrograms(user.getId());

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
                    Button btnView = (Button)v.findViewById(R.id.viewBtn);
                    btnView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent spIntent = new Intent(Programs.this, ProgramSingle.class);
                            spIntent.putExtra("PROG_ID", program.getId());
                            startActivity(spIntent);
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
}
