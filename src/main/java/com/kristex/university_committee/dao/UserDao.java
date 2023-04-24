package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Registration;
import com.kristex.university_committee.model.User;

import java.util.List;

public interface UserDao {

    User getUserById(int id);

    List<User> getAllUsers();
    List<User> getAllUsersByFacultyId(int id);
    List<User> getAllUnconfirmedUsers();
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getByEmail(String email);
    void confirmUser(int id);
}
