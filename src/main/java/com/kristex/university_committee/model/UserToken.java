package com.kristex.university_committee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserToken {
    int id;
    int userId;
    String access;
    String refresh;

    public UserToken(int userId, String access, String refresh){
        this.userId = userId;
        this.access = access;
        this.refresh = refresh;
    }
}
