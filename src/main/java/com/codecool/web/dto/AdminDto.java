package com.codecool.web.dto;

import com.codecool.web.model.User;

import java.util.List;

public class AdminDto {

    User user;
    List<User> users;

    public AdminDto(User user, List<User> users) {
        this.user = user;
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
