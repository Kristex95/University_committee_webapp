package com.kristex.university_committee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Result {
    Integer id;
    Integer abitId;
    Integer subjId;
    Float grade;

    public Result(Integer abitId, Integer subjId, Float grade){
        this.abitId = abitId;
        this.subjId = subjId;
        this.grade = grade;
    }
}
