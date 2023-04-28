package com.kristex.university_committee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {
    int id;
    String firstName;
    String lastName;
    String email;
    Role role;
    float school_mark;
    int math_mark;
    int english_mark;
    int history_mark;
    String cache;

    boolean confirmed;
    String type_auth;

    public User(String firstName, String lastName, String email, String cache){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cache = cache;
    }

    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String firstName, String lastName, String email, Role role, float school_mark, int math_mark, int english_mark, int history_mark) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.school_mark = school_mark;
        this.math_mark = math_mark;
        this.english_mark = english_mark;
        this.history_mark = history_mark;
    }
}

