package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.UserToken;

import java.util.List;

public interface UserTokenDao {

    UserToken getById(int id);
    List<UserToken> getAll();
    void createToken(UserToken userToken);
    void updateToken(UserToken userToken);
    void deleteToken(int id);

    UserToken getByUserId(int id);

}
