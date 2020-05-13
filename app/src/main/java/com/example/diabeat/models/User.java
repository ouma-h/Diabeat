package com.example.diabeat.models;

public class User {
    private String firstName;
    private String lastName;
    private String gender;
    private String birthdate;


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setFirstName(String firstName) {       this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public User(String firstName, String lastName, String gender, String birthdate ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthdate = birthdate;
    }


}
