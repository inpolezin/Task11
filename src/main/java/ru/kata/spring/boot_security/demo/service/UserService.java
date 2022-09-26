package com.polezin.spring.service;

import com.polezin.spring.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    void saveUser(User user);
    User findUserById(Long id);
    void updateUser(User user);
    void deleteUserById(Long id);
}
