package com.codecool.web.model;

public class User extends AbstractModel {

    private String email;
    private String userName;
    private String password;
    private String role;

    public User(int id, String email, String userName, String password, String role) {
        super(id);
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
