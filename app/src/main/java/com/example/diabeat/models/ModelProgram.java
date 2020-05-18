package com.example.diabeat.models;

import java.util.List;

public class ModelProgram {
    private Integer id;
    private  String condition;
    private Integer duration;
    private  String duration_unit;
    private  String start_date;
    private Integer user_id;
    private List<Integer> meds;

    public Integer getId() {
        return id;
    }

    public String getCondition() {
        return condition;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    public String getStart_date() {
        return start_date;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public List<Integer> getMeds() {
        return meds;
    }

    public ModelProgram(String condition, Integer duration, String duration_unit, String start_date, Integer user_id) {
        this.condition = condition;
        this.duration = duration;
        this.duration_unit = duration_unit;
        this.start_date = start_date;
        this.user_id = user_id;
    }
}
