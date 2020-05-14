package com.example.diabeat.models;

import com.google.gson.annotations.SerializedName;

public class Medication {

    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private Integer amount;
    @SerializedName("category")
    private String category;
    @SerializedName("duration")
    private Integer duration;
    @SerializedName("duration_unit")
    private String duration_unit;
    @SerializedName("isBefore")
    private Boolean isBefore;
    @SerializedName("prog_id")
    private Integer prog_id;

    Medication() {

    }

    public Medication(String name, Integer amount, String category, Integer duration, String duration_unit, Boolean isBefore, Integer prog_id) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.duration = duration;
        this.duration_unit = duration_unit;
        this.isBefore = isBefore;
        this.prog_id = prog_id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    public Boolean getBefore() {
        return isBefore;
    }

    public Integer getProg_id() {
        return prog_id;
    }
}
