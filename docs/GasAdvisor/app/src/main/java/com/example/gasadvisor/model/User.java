package com.example.gasadvisor.model;

public class User {
    Integer _id;
    String username;
    String password;
    String email;
    String name;
    String lastName;

    public User(String username, String email, String name, String lastName) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

    public User(String username, String password, String email, String name, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

    public Integer get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }
}