package com.kristex.university_committee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Subject {
    int id;
    String name;

    public Subject(String name){
        this.name = name;
    }
}
