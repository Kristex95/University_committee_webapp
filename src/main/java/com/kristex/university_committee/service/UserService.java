package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.UserDaoImpl;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.UserToken;
import com.kristex.university_committee.utils.JWTUtils;
import org.json.JSONArray;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

public class UserService {

    private static UserService instance;

    public static synchronized UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }

    public User getUserById(int id){
        return UserDaoImpl.getInstance().getUserById(id);
    }

    public List<User> getAllUser(){
        return  UserDaoImpl.getInstance().getAllUsers();
    }
    public List<User> getAllUsersByFacultyId(int id){
        return UserDaoImpl.getInstance().getAllUsersByFacultyId(id);
    }

    public List<User> getAllUnconfirmedUsers(){
        return UserDaoImpl.getInstance().getAllUnconfirmedUsers();
    }
    public static void createUser(User user){
        UserDaoImpl.getInstance().createUser(user);
    }
    public static void updateUser(User user){
        UserDaoImpl.getInstance().updateUser(user);
    }
    public static void deleteUser(int id){
        UserDaoImpl.getInstance().deleteUser(id);
    }

    public static String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedByte = digest.digest(password.getBytes("UTF-8"));
            String hashedPassword = Base64.getEncoder().encodeToString(hashedByte);
            return hashedPassword;
        }catch (Exception e){
            System.out.println("hash :" + e);
            return null;
        }
    }

    public static User getByEmail(String email, String auth_type){
        return UserDaoImpl.getInstance().getByEmail(email, auth_type);
    }

    public static UserToken login(String email, String pass){

        return null;
    }

    public static void confirmUser(int id) {
        UserDaoImpl.getInstance().confirmUser(id);
    }
    public static JSONArray getAcceptedUsersJSON(int faculty_id){
        return UserDaoImpl.getInstance().getAcceptedUsers(faculty_id);
    }
}
