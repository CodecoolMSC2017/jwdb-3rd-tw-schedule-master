package com.codecool.web.dto;

import com.codecool.web.model.User;

import java.util.List;

public class AdminDto {

    private User user;
    private List<User> users;

    public AdminDto(User user, List<User> users) {
        this.user = user;
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public List<User> getUsers() {
        return users;
    }
}
