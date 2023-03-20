package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.UserDaoImpl;
import com.kristex.university_committee.model.User;

import java.util.List;

public class UserService {

    private static UserService instance;

    public static synchronized UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }

    public User GetUserById(int id){
        return UserDaoImpl.getInstance().GetUserById(id);
    }

    public List<User> GetAllUser(){
        return  UserDaoImpl.getInstance().GetAllUsers();
    }
}
