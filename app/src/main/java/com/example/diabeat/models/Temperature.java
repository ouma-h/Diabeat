package com.example.diabeat.models;


public class Temperature {
    private Integer user_id;
    private String temp;
    private String temp_date;

    Temperature(){}

    public Temperature(Integer user_id, String temp, String temp_date) {
        this.user_id = user_id;
        this.temp = temp;
        this.temp_date = temp_date;
    }

    public String getTemp_date() {
        return temp_date;
    }

    public void setTemp_date(String temp_date) {
        this.temp_date = temp_date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
