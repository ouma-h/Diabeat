package com.example.diabeat.models;

public class User {
    private Integer id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String birthday;

    private String password;
    private String authToken;


    User() {
    }

    public User(String email, String password, String username) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String firstName, String lastName, String username) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.first_name = firstName;
        this.last_name = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }


    public void setBirthday(String birthdate) {
        this.birthday = birthdate;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getId() { return id; }

}
