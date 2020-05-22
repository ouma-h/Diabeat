package com.example.diabeat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.apiBackend.HealthStatusApi;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.BloodPressure;
import com.example.diabeat.models.Temperature;
import com.example.diabeat.models.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthStatus extends AppCompatActivity implements View.OnClickListener {

    HealthStatusApi healthStatusApi;
    TextView temperature;
    SimpleDateFormat display;
    SimpleDateFormat apiFormat;
    TextView diastolic;
    TextView systolic;
    TextView temp_date;
    TextView bp_date;
    User user;
    List<Temperature> TempList;
    String[] dateValues;
    LineChart myChart;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_status);

        healthStatusApi = RetrofitClientInstance.getHealthStatusApi();



        display = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
        apiFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        user = MainActivity.getUserInfo(this);

        myChart = findViewById(R.id.line_chart_temp);
        findViewById(R.id.back_arrow).setOnClickListener(this);
        findViewById(R.id.addTemperature).setOnClickListener(this);
        findViewById(R.id.addBloodPressure).setOnClickListener(this);

        temperature = findViewById(R.id.temp_measure);
        diastolic = findViewById(R.id.diastolic);
        systolic = findViewById(R.id.systolic);
        temp_date = findViewById(R.id.temp_date);
        bp_date = findViewById(R.id.bp_date);

        getTemperatures(user.getId());
        getBloodPressures(user.getId());

        try {
            getTempInfo(this, temperature, temp_date, apiFormat,display);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            getBloodPressureInfo(this, systolic, diastolic, bp_date, apiFormat,display);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public String checkBloodPressure(String sys, String dias) {
        Float s = Float.parseFloat(sys);
        Float d = Float.parseFloat(dias);
        if (s <= 90) {
            if (d <= 60) {
                return "low";
            } else if (d <= 80) {
                return "ideal";
            } else if (d <= 90) {
                return "pre-high";
            } else if (d <= 100) {
                return "high";
            }
        } else if (s <= 120) {
            if (d <= 60) {
                return "low";
            } else if (d <= 80) {
                return "ideal";
            } else if (d <= 90) {
                return "pre-high";
            } else if (d <= 100) {
                return "high";
            }
        } else if (s <= 140) {
            if (d <= 60) {
                return "low";
            } else if (d <= 80) {
                return "ideal";
            } else if (d <= 90) {
                return "pre-high";
            } else if (d <= 100) {
                return "high";
            }
        } else if (s <= 190) {
            if (d <= 60) {
                return "low";
            } else if (d <= 80) {
                return "ideal";
            } else if (d <= 90) {
                return "pre-high";
            } else if (d <= 100) {
                return "high";
            }
        }
        return "non_valid";
    }

    public void addTemperature(String temp, String temp_date) {


        final Temperature temperatureobj = new Temperature(user.getId(), temp, temp_date);
        Call<Temperature> call = healthStatusApi.addTemperature(temperatureobj);
        call.enqueue(new Callback<Temperature>() {
            @Override
            public void onResponse(Call<Temperature> call, Response<Temperature> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HealthStatus.this, "code: "+ response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                setLastTemp(HealthStatus.this,temperatureobj);
            }

            @Override
            public void onFailure(Call<Temperature> call, Throwable t) {
                Toast.makeText(HealthStatus.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    @SuppressLint("ResourceAsColor")
    public void addBloodPressure(String sys, String dias, String bloodPressure_date) {

        final BloodPressure bloodPressure = new BloodPressure(user.getId(),sys , dias,  bloodPressure_date);

        Call<BloodPressure> call = healthStatusApi.addBloodPressure(bloodPressure);
        call.enqueue(new Callback<BloodPressure>() {
            @Override
            public void onResponse(Call<BloodPressure> call, Response<BloodPressure> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HealthStatus.this, "code: "+ response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                setLastBloodPressure(HealthStatus.this, bloodPressure);
            }

            @Override
            public void onFailure(Call<BloodPressure> call, Throwable t) {
                Toast.makeText(HealthStatus.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }


    public void getTemperatures(Integer user_id) {
        Call<List<Temperature>> call = healthStatusApi.getTemperatures(user_id);

        call.enqueue(new Callback<List<Temperature>>() {
            @Override
            public void onResponse(Call<List<Temperature>> call, Response<List<Temperature>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HealthStatus.this, "code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                TempList = response.body();
                TemperatureValues();

            }

            @Override
            public void onFailure(Call<List<Temperature>> call, Throwable t) {
                Toast.makeText(HealthStatus.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getBloodPressures(Integer user_id){
        Call<List<BloodPressure>> call = healthStatusApi.getBloodPressures(user_id);
        call.enqueue(new Callback<List<BloodPressure>>() {
            @Override
            public void onResponse(Call<List<BloodPressure>> call, Response<List<BloodPressure>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HealthStatus.this, "code: "+ response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                List<BloodPressure> bloodPressures = response.body();
                for(final BloodPressure bloodPressure : bloodPressures ) {
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = vi.inflate(R.layout.blood_pressure_card, null, false);

                    // Add Value
                    TextView systolic = (TextView) view.findViewById(R.id.systolic_card);
                    systolic.setText(bloodPressure.getSystolic());
                    TextView diastolic = (TextView) view.findViewById(R.id.diastolic_card);
                    diastolic.setText(bloodPressure.getDiastolic());
                    TextView date = (TextView) view.findViewById(R.id.bp_date_card);
                    date.setText(bloodPressure.getBloodPressure_date());

                    ViewGroup insertPoint = (ViewGroup) findViewById(R.id.blood_pressure_list);
                    insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                }
            }

            @Override
            public void onFailure(Call<List<BloodPressure>> call, Throwable t) {

            }
        });

    }

    public void openTempDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                HealthStatus.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.add_temperature_dialog,
                (LinearLayout) findViewById(R.id.temp_bottom_sheet));

        bottomSheetView.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp = bottomSheetView.findViewById(R.id.temp);
                String tp = temp.getText().toString();
                addTemperature(tp, currentDateTime(apiFormat));
                temperature.setText(tp);
                temp_date.setText(currentDateTime(display));
                myChart.getData().notifyDataChanged();
                myChart.notifyDataSetChanged();
                bottomSheetDialog.dismiss();
                recreate();


            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();


    }

    public void openBPDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                HealthStatus.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.add_blood_pressure,
                (LinearLayout) findViewById(R.id.blood_pressure_bottom_sheet));

        bottomSheetView.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.plusBP).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                EditText vdiastolic = bottomSheetView.findViewById(R.id.diastolic);
                EditText vsystolic = bottomSheetView.findViewById(R.id.systolic);
                String d = vdiastolic.getText().toString();
                String s = vsystolic.getText().toString();
                diastolic.setText(d);
                systolic.setText(s);
                bp_date.setText(currentDateTime(display));
                bottomSheetDialog.dismiss();
                addBloodPressure(s, d, currentDateTime(apiFormat));


                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = vi.inflate(R.layout.blood_pressure_card, null, false);

                // Add Value
                TextView systolic = (TextView) view.findViewById(R.id.systolic_card);
                systolic.setText(s);
                TextView diastolic = (TextView) view.findViewById(R.id.diastolic_card);
                diastolic.setText(d);
                TextView date = (TextView) view.findViewById(R.id.bp_date_card);
                ImageView icon = view.findViewById(R.id.icon_bp);
                date.setText(bp_date.getText());
                switch (checkBloodPressure(s, d)){

                    case "low":
                        icon.setBackgroundTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));
                        break;
                    case "ideal":
                        icon.setBackgroundTintList(ColorStateList.valueOf(R.color.ButtonColor));
                        break;
                    case "pre-high":
                        icon.setBackgroundTintList(ColorStateList.valueOf(R.color.pre_high));
                        break;
                    case "high":
                        icon.setBackgroundTintList(ColorStateList.valueOf(R.color.high));
                        break;

                }

                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.blood_pressure_list);
                insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));





            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }


//    \*************************************** TEMPERATURE CHART *****************************************************\


    public void setLineGraph(ArrayList<Entry> yValues, String[] values) {

        myChart.setTouchEnabled(true);
        myChart.setPinchZoom(true);

        myChart.getAxisRight().setEnabled(false);

        YAxis leftAxis = myChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(42f);
        leftAxis.setAxisMinimum(30f);

        XAxis xAxis = myChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineDataSet set1 = new LineDataSet(yValues, "Temperature");

        set1.setFillAlpha(500);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setColor(Color.rgb(0, 29, 95));
        set1.setLineWidth(2f);
        set1.setCircleColor(Color.rgb(177, 210, 175));
        set1.setDrawCircleHole(true);
        set1.setCircleHoleColor(Color.rgb(0, 29, 95));
        set1.setValueTextSize(8f);
        set1.setValueTextColor(R.color.colorPrimary);
        set1.setFillColor(Color.rgb(177, 210, 175));
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        myChart.notifyDataSetChanged();
        myChart.setData(data);
        myChart.invalidate();
    }


    public class MyXAxisValueFormatter extends ValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {

            this.mValues = values;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }


    private void TemperatureValues() {
        ArrayList<Entry> yValues = new ArrayList<>();
        dateValues = new String[TempList.size()];
        for (int i = 0; i < TempList.size(); i++) {

            yValues.add(new Entry(i, Float.parseFloat(TempList.get(i).getTemp())));
            dateValues[i] = (/*TempList.get(i).getTemp_date().substring(0,10)+" "+*/TempList.get(i).getTemp_date().substring(11, 16));
        }
        setLineGraph(yValues, dateValues);
    }

    //    \*************************************** ************* *****************************************************\




    //saving the last temperature taken
    public static void setLastTemp(@NonNull Context context, Temperature temperature) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String tempJson = gson.toJson(temperature);
        editor.putString("TEMP", tempJson);
        editor.apply();
    }

    //saving the last bp taken
    public static void setLastBloodPressure(@NonNull Context context, BloodPressure bloodPressure) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String bloodPressureString = gson.toJson(bloodPressure);
        editor.putString("BLOODPRESSURE", bloodPressureString);
        editor.apply();
    }

    public static void getTempInfo(@NonNull Context context, TextView temperature, TextView temp_date, SimpleDateFormat input, SimpleDateFormat output) throws ParseException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("TEMP", "");
        Temperature temp = gson.fromJson(json, Temperature.class);
        if(temp != null ){
            temperature.setText(temp.getTemp());
            Date date = input.parse(temp.getTemp_date());
            String outputText = output.format(date);
            temp_date.setText(outputText);
        }

    }

    public static void getBloodPressureInfo(@NonNull Context context, TextView sys, TextView dias, TextView bpDate, SimpleDateFormat input, SimpleDateFormat output) throws ParseException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("BLOODPRESSURE", "");
        BloodPressure bp = gson.fromJson(json, BloodPressure.class);
        if (bp != null){

            sys.setText(bp.getSystolic());
            dias.setText(bp.getDiastolic());
            Date date = input.parse(bp.getBloodPressure_date());
            String outputText = output.format(date);
            bpDate.setText(outputText);
        }
    }

    public String currentDateTime(SimpleDateFormat s) {
        Calendar calendar = Calendar.getInstance();
        return (s.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.addTemperature:
                openTempDialog();
                break;
            case R.id.addBloodPressure:
                openBPDialog();
                break;

        }
    }
}
