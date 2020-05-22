package com.example.diabeat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabeat.apiBackend.DoctorAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Appointment;
import com.example.diabeat.models.Doctor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppointmentCardsSlider extends RecyclerView.Adapter<AppointmentCardsSlider.AppointmentCardsViewHolder>{

    static DoctorAPI doctorAPI;
    static Doctor doctor;
    private List<Appointment> appointmentList;



    public AppointmentCardsSlider(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new AppointmentCardsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.appointment_card, parent, false
                )
        );
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AppointmentCardsViewHolder holder, int position) {
        try {
            holder.setAppointmentData(appointmentList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentCardsViewHolder extends RecyclerView.ViewHolder{

        private TextView doctorName, specialty, dateAppointment;


        public AppointmentCardsViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctor_name);
            specialty = itemView.findViewById(R.id.doctor_specialty);
            dateAppointment = itemView.findViewById(R.id.date_appointment);
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        void setAppointmentData(Appointment appointment) throws ParseException {


            doctorAPI = RetrofitClientInstance.getDoctorApi();
            Call<Doctor> call = doctorAPI.getDoctor(appointment.getDoc());

            call.enqueue(new Callback<Doctor>() {
                @Override
                public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                    doctor = response.body();
                    doctorName.setText(doctor.getName());
                    specialty.setText(doctor.getSpeciality());

                }

                @Override
                public void onFailure(Call<Doctor> call, Throwable t) {
                }
            });
            @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(appointment.getAppDate());
            String outputText = outputFormat.format(date);
            dateAppointment.setText(outputText);
        }


    }

}
