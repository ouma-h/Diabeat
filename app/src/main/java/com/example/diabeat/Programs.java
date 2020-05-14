package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Programs extends AppCompatActivity {
    private TextView resultView;
    LinearLayout layoutparent;
    private View v;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);

        resultView = findViewById(R.id.responseView);
        layoutparent = findViewById(R.id.linearCards);
        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.program_card, null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://diabeatbackend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProgramAPI apiHolder = retrofit.create(ProgramAPI.class);
        Call<List<ModelProgram>> call = apiHolder.getPrograms(3);

        call.enqueue(new Callback<List<ModelProgram>>() {

            @Override
            public void onResponse(Call<List<ModelProgram>> call, Response<List<ModelProgram>> response) {
                resultView.setText("code :"+ response.code()); // RESPONSE CODE FOR DEBUGGING
                List<ModelProgram> programs = response.body();
                for(final ModelProgram program : programs) {
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
                            Toast.makeText(getApplicationContext(),"ID: "+program.getCondition(),Toast.LENGTH_LONG).show();
                        }
                    });

                    layoutparent.addView(v);
                }
            }

            @Override
            public void onFailure(Call<List<ModelProgram>> call, Throwable t) {
                resultView.setText(t.getMessage());
            }
        });

    }
}
