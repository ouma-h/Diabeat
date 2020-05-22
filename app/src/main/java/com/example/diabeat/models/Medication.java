package com.example.diabeat.models;

import com.google.gson.annotations.SerializedName;

public class Medication {

    private Integer id;
    private String name;
    private Integer amount;
    private String category;
    private Integer duration;
    private String duration_unit;
    private Boolean isMon;
    private Boolean isTue;
    private Boolean isWed;
    private Boolean isThu;
    private Boolean isFri;
    private Boolean isSat;
    private Boolean isSun;
    private String morning;
    private String midday;
    private String night;
    private Boolean isBefore;
    private Integer prog_id;
    private Integer user_id;

    Medication() {

    }

    public Medication(String name, Integer amount, String category, Integer duration, String duration_unit, Boolean isMon, Boolean isTue, Boolean isWed, Boolean isThu, Boolean isFri, Boolean isSat, Boolean isSun, String morning, String midday, String night, Boolean isBefore, Integer prog_id, Integer user_id) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.duration = duration;
        this.duration_unit = duration_unit;
        this.isMon = isMon;
        this.isTue = isTue;
        this.isWed = isWed;
        this.isThu = isThu;
        this.isFri = isFri;
        this.isSat = isSat;
        this.isSun = isSun;
        this.morning = morning;
        this.midday = midday;
        this.night = night;
        this.isBefore = isBefore;
        this.prog_id = prog_id;
        this.user_id = user_id;
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

    public Boolean getMon() {
        return isMon;
    }

    public Boolean getTue() {
        return isTue;
    }

    public Boolean getWed() {
        return isWed;
    }

    public Boolean getThu() {
        return isThu;
    }

    public Boolean getFri() {
        return isFri;
    }

    public Boolean getSat() {
        return isSat;
    }

    public Boolean getSun() {
        return isSun;
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

    public Boolean getBefore() {
        return isBefore;
    }

    public Integer getProg_id() {
        return prog_id;
    }
    public Integer getUser_id() {
        return user_id;
    }
}
