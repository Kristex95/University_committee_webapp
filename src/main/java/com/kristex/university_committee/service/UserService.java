package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.UserDaoImpl;
import com.kristex.university_committee.dao.impl.UserTokenDaoImpl;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.UserToken;
import com.kristex.university_committee.utils.JWTUtils;

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

    public static User getByEmail(String email){
        return UserDaoImpl.getInstance().getByEmail(email);
    }

    public static UserToken login(String email, String pass){
        User user = getByEmail(email);
        if(user.getCache().equals(hashPassword(pass))){
            UserTokenService.createUserToken(new UserToken(
                    user.getId(),
                    JWTUtils.createAccessToken(String.valueOf(user.getId()), user.getRole()),
                    JWTUtils.createRefreshToken(String.valueOf(user.getId()), user.getRole())));
            return UserTokenDaoImpl.getInstance().getByUserId(user.getId());
        }
        else {
            System.out.println("wrong password");
            return null;
        }

    }
}
