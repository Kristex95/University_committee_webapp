package com.kristex.university_committee.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Faculty {

    int id;
    String name;

    public Faculty(String name){
        this.name = name;
    }
}
