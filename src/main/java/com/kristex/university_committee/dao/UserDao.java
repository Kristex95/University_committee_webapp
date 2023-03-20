package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.User;

import java.util.List;

public interface UserDao {

    public User GetUserById(int id);

    public List<User> GetAllUsers();
}
