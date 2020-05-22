package com.example.diabeat;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabeat.models.Medication;

import java.util.List;



public class MedicationCardsSlider extends RecyclerView.Adapter<MedicationCardsSlider.MedicationCardsViewHolder> {

    private List<Medication> medicationsList;

    public MedicationCardsSlider(List<Medication> body) {
        this.medicationsList = body;
        System.out.println(medicationsList);
    }



    @NonNull
    @Override
    public MedicationCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MedicationCardsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.medication_reminder_card, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationCardsViewHolder holder, int position) {
        holder.setMedicationData(medicationsList.get(position));
    }

    @Override
    public int getItemCount() {
        return medicationsList.size();
    }

    static class MedicationCardsViewHolder extends RecyclerView.ViewHolder {

        private TextView medName, instructions, icon;

        public MedicationCardsViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medName);
            instructions = itemView.findViewById(R.id.instuctions);
            icon = itemView.findViewById(R.id.icon);
        }


        void setMedicationData(Medication medication) {

            System.out.println(medication.getName());
            medName.setText(medication.getName());
            String inst;
            inst = medication.getAmount().toString();
            switch (medication.getCategory()) {
                case "Inhaled":
                    inst += " Inhalation(s)";
                    icon.setBackgroundResource(R.drawable.inhaling);
                    break;
                case "Pills":
                    inst += " Pill(s)";
                    icon.setBackgroundResource(R.drawable.ic_bottle_pills);
                    break;
                case "Ointment":
                    inst += " Ointment(s)";
                    icon.setBackgroundResource(R.drawable.ic_cream);
                    break;
                case "Syringe":
                    inst += " Syringe(s)";
                    icon.setBackgroundResource(R.drawable.ic_injection);
                    break;
                default:
                    break;
            }

            if (medication.getBefore()) {
                inst += ", before meal.";
            } else {
                inst += ", after meal.";
            }

            instructions.setText(inst);
        }
    }
}

