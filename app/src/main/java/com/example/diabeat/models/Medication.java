package com.example.diabeat.models;

public class Medication {

    private Integer id;
    private String name;
    private String amount;
    private String category;
    private Integer duration;
    private String duration_unit;
    private Boolean isBefore;
    private Integer prog_id;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
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
