package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Registration;
import com.kristex.university_committee.model.User;
import org.json.JSONArray;

import java.util.List;

public interface UserDao {

    User getUserById(int id);

    List<User> getAllUsers();
    List<User> getAllUsersByFacultyId(int id);
    List<User> getAllUnconfirmedUsers();
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getByEmail(String email, String auth_type);
    void confirmUser(int id);
    JSONArray getAcceptedUsers(int faculty_id);
}
