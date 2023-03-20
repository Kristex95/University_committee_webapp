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
    float schoolGrade;
    int facultyId;
    String email;
    Role role;
    String cache;
    String salt;
}
