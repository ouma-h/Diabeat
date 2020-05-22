package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diabeat.models.Medication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Gson gson = new Gson();
        String json = "["+getHistory()+"]";
        Log.d("JSON", json);
        List<Medication> meds = gson.fromJson(json, new TypeToken<List<Medication>>(){}.getType());
        String toshow = "";
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
            durationView.setText(med.getMorning().toString());

            if(med.getIs_discarded()) {
                CardView card = v.findViewById(R.id.medCard);
                card.setCardBackgroundColor(Color.rgb(255,204,203));
            }

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearCards);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }
    }
    private String getHistory(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String hist = sharedPreferences.getString("HISTORY", "");

        return hist;
    }
}
