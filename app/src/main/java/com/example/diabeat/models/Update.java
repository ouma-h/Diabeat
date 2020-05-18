package com.example.diabeat.models;

public class Update {
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String birthdate;
    private String password;

    public Update(String email, String first_name, String last_name, String birthdate) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthdate = birthdate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getBirthdate() {
        return birthdate;
    }

}
