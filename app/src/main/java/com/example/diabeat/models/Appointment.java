package com.example.diabeat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("app_date")
    @Expose
    private String appDate;
    @SerializedName("user")
    @Expose
    private Integer user;
    @SerializedName("doc")
    @Expose
    private Integer doc;

    public Appointment(String appDate, Integer user, Integer doc) {
        this.appDate = appDate;
        this.user = user;
        this.doc = doc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getDoc() {
        return doc;
    }

    public void setDoc(Integer doc) {
        this.doc = doc;
    }

}