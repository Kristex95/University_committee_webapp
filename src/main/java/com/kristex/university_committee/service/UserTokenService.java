package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.UserTokenDaoImpl;
import com.kristex.university_committee.model.UserToken;

import java.util.List;

public class UserTokenService {

    public static UserToken getById(int id){
        return UserTokenDaoImpl.getInstance().getById(id);
    }

    public static List<UserToken> getAll(){
        return UserTokenDaoImpl.getInstance().getAll();
    }

    public static void createUserToken(UserToken userToken){
        UserTokenDaoImpl.getInstance().createToken(userToken);
    }

    public static void updateUserToken(UserToken userToken){
        UserTokenDaoImpl.getInstance().updateToken(userToken);
    }

    public static void deleteUserToken(int id){
        UserTokenDaoImpl.getInstance().deleteToken(id);
    }

    public static UserToken getByUserId(int id){
        return UserTokenDaoImpl.getInstance().getByUserId(id);
    }
}
