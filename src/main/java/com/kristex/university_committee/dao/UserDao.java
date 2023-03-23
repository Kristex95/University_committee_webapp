package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.User;

import java.util.List;

public interface UserDao {

    User getUserById(int id);

    List<User> getAllUsers();
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getByEmail(String email);
}
