package com.example.diabeat;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabeat.apiBackend.ProgramAPI;
import com.example.diabeat.apiBackend.RetrofitClientInstance;
import com.example.diabeat.models.Medication;
import com.example.diabeat.models.ModelProgram;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicationCardsSlider extends RecyclerView.Adapter<MedicationCardsSlider.MedicationCardsViewHolder> {

    private List<ModelProgram> programs;

    public MedicationCardsSlider(List<ModelProgram> body) {
        this.programs = body;
    }

    @NonNull
    @Override
    public MedicationCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MedicationCardsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.medication_card, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationCardsViewHolder holder, int position) {
       /* try {
            holder.setMedicationData(appointmentList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class MedicationCardsViewHolder extends RecyclerView.ViewHolder{

        ProgramAPI programApi;
        private TextView program_card, medName, instructions, icon;

        public MedicationCardsViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medName);
            program_card = itemView.findViewById(R.id.program);
            instructions = itemView.findViewById(R.id.instuctions);
            icon = itemView.findViewById(R.id.icon);
        }


        void setMedicationData(List<ModelProgram> programs ) throws ParseException {

            for(final ModelProgram program : programs) {

                programApi = RetrofitClientInstance.getProgramAPI();
                Call<List<Medication>> call = programApi.getProgramMeds(program.getId());
                call.enqueue(new Callback<List<Medication>>() {

                    @Override
                    public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
                        List<Medication> meds = response.body();
                        if(meds != null){
                            for(final Medication medication : meds) {
                                program_card.setText(program.getCondition());
                                medName.setText(medication.getName());

                                String inst ;
                                inst = medication.getAmount().toString();
                                switch(medication.getCategory()){
                                    case "Inhaled":
                                        inst += "Inhalation(s)";
                                        icon.setBackgroundResource(R.drawable.inhaling);
                                        break;
                                    case "Pills":
                                        inst += "Pill(s)";
                                        icon.setBackgroundResource(R.drawable.ic_bottle_pills);
                                        break;
                                    case "Ointment":
                                        inst += "Ointment(s)";
                                        icon.setBackgroundResource(R.drawable.ic_cream);
                                        break;
                                    case "Syringe":
                                        inst += "Syringe(s)";
                                        icon.setBackgroundResource(R.drawable.ic_injection);
                                        break;
                                    default:
                                        break;
                                }

                                if(medication.getBefore()){
                                    inst += ", before meal.";
                                }else {
                                    inst += ", after meal.";
                                }

                                instructions.setText(inst);
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Medication>> call, Throwable t) {
                    }
                });
            }



            /*@SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(appointment.getAppDate());
            String outputText = outputFormat.format(date);
            dateAppointment.setText(outputText);*/
        }
    }
}
