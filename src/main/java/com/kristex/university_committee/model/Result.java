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
    Integer abit_id;
    Integer subj_id;
    Float grade;
}
