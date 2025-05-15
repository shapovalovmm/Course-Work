package com.project.SH.service;

import com.project.SH.model.impl.User;

import java.util.List;

public interface IUserService {
    List<User> findAllUsers();
    User saveUser(User user);
    User findByID(int userID);
    User updateUser(User user);
    void deleteUser(int userID);
}
