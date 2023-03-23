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
    String cache;

    public User(String firstName, String lastName, String email, Role role, String cache){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.cache = cache;
    }
}

