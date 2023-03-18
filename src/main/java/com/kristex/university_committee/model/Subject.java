package com.kristex.university_committee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Subject {
    int id;
    String name;

    public Subject(int id, String name){
        this.id = id;
        this.name = name;
    }
}
