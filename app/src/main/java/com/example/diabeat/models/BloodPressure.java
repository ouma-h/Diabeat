package com.example.diabeat.models;


public class BloodPressure {
    private Integer user_id;
    private String diastolic;
    private String systolic;
    private String bloodPressure_date;

    BloodPressure() {}

    public BloodPressure(Integer user_id, String systolic, String diastolic, String bloodPressure_date) {
        this.user_id = user_id;
        this.diastolic = diastolic;
        this.systolic = systolic;
        this.bloodPressure_date = bloodPressure_date;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public String getSystolic() {
        return systolic;
    }

    public String getBloodPressure_date() {
        return bloodPressure_date;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public void setSystolic(String systolic) {
        systolic = systolic;
    }

    public void setBloodPressure_date(String bloodPressure_date) {
        this.bloodPressure_date = bloodPressure_date;
    }

}
