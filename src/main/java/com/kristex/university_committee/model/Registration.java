package com.kristex.university_committee.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Registration {
    int id;
    int userId;
    int facultyId;


    public Registration(int userId, int facultyId){
        this.userId = userId;
        this.facultyId = facultyId;

    }
}
