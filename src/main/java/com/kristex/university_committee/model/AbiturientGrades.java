package com.kristex.university_committee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AbiturientGrades {
    Abiturient abiturient;
    String faculty;
    Map<String, Integer> certificate_grades;
}
