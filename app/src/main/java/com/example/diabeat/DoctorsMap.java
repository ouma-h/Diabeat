package com.example.diabeat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabeat.apiBackend.AppointmentAPI;
import com.example.diabeat.apiBackend.DoctorAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Appointment;
import com.example.diabeat.models.Doctor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsMap extends FragmentActivity implements OnMapReadyCallback {

    private static final int TAG_CODE_PERMISSION_LOCATION = 0;
    private static final int REQUEST_PHONE_CALL = 0;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LatLng myLocation;
    // DATE TIME PICKER CODE SNIPPET
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Calendar c;
    private Context ctx = this;
    private TextView dateinput;
    private DoctorAPI doctorAPIholder;
    private AppointmentAPI appointmentAPIholder;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);

        // DATE PICKER
        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);

        // Doctor APIHOLDER
        doctorAPIholder = RetrofitClientInstance.getDoctorApi();

        // Appointment APIHOLDER
        appointmentAPIholder = RetrofitClientInstance.getAppointmentApi();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            showDoctors();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }


    }

    public void openDoctorDialog(final Doctor doc) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                DoctorsMap.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.doctor_modal,
                (LinearLayout) findViewById(R.id.dialog));
        ImageButton btnSMS = bottomSheetView.findViewById(R.id.btnSMS);
        ImageButton btnCall = bottomSheetView.findViewById(R.id.btnCall);

        btnSMS.setImageResource(R.drawable.ic_email);
        btnCall.setImageResource(R.drawable.ic_telephone);

        // UPDATING TEXT
        TextView title = bottomSheetView.findViewById(R.id.doctorName);
        title.setText(doc.getName());

        TextView docspe = bottomSheetView.findViewById(R.id.doctorSpe);
        docspe.setText(doc.getSpeciality());

        TextView docab = bottomSheetView.findViewById(R.id.doctorAbout);
        docab.setText(doc.getAbout());

        TextView docOp = bottomSheetView.findViewById(R.id.doctorOpening);
        docOp.setText(doc.getOpeninghours());

        Button newAppointement = bottomSheetView.findViewById(R.id.btnNewApp);
        newAppointement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                openDoctorAppointement(doc.getId());
            }
        });

        ImageButton btnMakeCall = bottomSheetView.findViewById(R.id.btnCall);
        btnMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(doc.getPhone());
            }
        });

        ImageButton btnSendSMS = bottomSheetView.findViewById(R.id.btnSMS);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(doc.getPhone());
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    public void openDoctorAppointement(final Integer docID) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                DoctorsMap.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.doctor_appointemen_modal,
                (LinearLayout) findViewById(R.id.dialog));

        dateinput = bottomSheetView.findViewById(R.id.inDate);
        dateinput.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                show_Datepicker();
            }
        });
        dateinput.requestFocus();
        Button conApp = bottomSheetView.findViewById(R.id.addApp);
        conApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAppointment(docID);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void showDoctors() {
        mMap.clear();
        Call<List<Doctor>> call = doctorAPIholder.getDoctors();
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                List<Doctor> doctors = response.body();
                for (final Doctor doctor : doctors) {
                    LatLng sydney = new LatLng(Float.parseFloat(doctor.getLat()), Float.parseFloat(doctor.getLng()));
                    mMap.addMarker(new MarkerOptions().position(sydney)
                            .title(doctor.getName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.doctor));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            openDoctorDialog(doctor);
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

            }
        });

    }

    private void addAppointment(Integer docID) {
        String dateapp = mYear + "-" + mMonth + "-" + mDay;
        Appointment app = new Appointment(dateapp, MainActivity.getUserInfo(this).getId(), docID);
        Call<Appointment> call = appointmentAPIholder.createAppointment(app);
        call.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code :" + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Your appointement has been added", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void show_Datepicker() {
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth - 1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + monthOfYear + "-" + dayOfMonth;
                        mDay = dayOfMonth;
                        mMonth = monthOfYear;
                        mYear = year;
                        dateinput.setText(date);
                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
    }

    private void makeCall(String phoneNum) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DoctorsMap.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNum));
            startActivity(callIntent);
        }

    }
    private void sendSMS(String phoneNum) {
        Uri uri = Uri.parse("smsto:"+phoneNum);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "Bonjour docteur,");
        startActivity(it);
    }
}
