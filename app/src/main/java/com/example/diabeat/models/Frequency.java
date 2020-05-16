package com.example.diabeat.models;

import com.google.gson.annotations.SerializedName;

public class Frequency {
    @SerializedName("morning")
    private String morning;
    @SerializedName("midday")
    private String midday;
    @SerializedName("night")
    private String night;
    @SerializedName("med_id")
    private Integer med_id;

    public Frequency(String morning, String midday, String night, Integer med_id) {
        this.morning = morning;
        this.midday = midday;
        this.night = night;
        this.med_id = med_id;
    }

    public String getMorning() {
        return morning;
    }

    public String getMidday() {
        return midday;
    }

    public String getNight() {
        return night;
    }

    public Integer getMed_id() {
        return med_id;
    }
}
