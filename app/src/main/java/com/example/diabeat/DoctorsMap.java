package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class DoctorsMap extends FragmentActivity implements OnMapReadyCallback {

    private static final int TAG_CODE_PERMISSION_LOCATION = 0;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);
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
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }


    }
    public void openDoctorDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                DoctorsMap.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.doctor_modal,
                (LinearLayout)findViewById(R.id.dialog));
        ImageButton btnSMS = bottomSheetView.findViewById(R.id.btnSMS);
        ImageButton btnCall = bottomSheetView.findViewById(R.id.btnCall);

        btnSMS.setImageResource(R.drawable.ic_email);
        btnCall.setImageResource(R.drawable.ic_telephone);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }
    public void openDoctorAppointement() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                DoctorsMap.this, R.style.BottomSheetDialogTheme
        );
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.doctor_appointemen_modal,
                (LinearLayout)findViewById(R.id.dialog));
        ImageButton btnSMS = bottomSheetView.findViewById(R.id.btnSMS);
        ImageButton btnCall = bottomSheetView.findViewById(R.id.btnCall);

        btnSMS.setImageResource(R.drawable.ic_email);
        btnCall.setImageResource(R.drawable.ic_telephone);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }
    private void showDoctors(){
        mMap.clear();
        LatLng sydney = new LatLng(34.426108, 8.787994);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("DocMike")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.doctor));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(getApplicationContext(),"Marker clicked!",Toast.LENGTH_LONG).show();
                openDoctorDialog();
                return true;
            }
        });
    }
}
