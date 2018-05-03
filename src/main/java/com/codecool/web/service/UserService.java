package com.codecool.web.service;

public interface UserService {

    void register(String userName, String password, String email);

    void login(String email, String password);

    void findUserById(int id);


}
