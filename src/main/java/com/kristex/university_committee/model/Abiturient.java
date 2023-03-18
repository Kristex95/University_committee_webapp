package com.kristex.university_committee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Abiturient {
    int id;
    String first_name;
    String last_name;
    float school_grade;
    int faculty_id;

    public Abiturient(String first_name, String last_name, float school_grade, int faculty_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.school_grade = school_grade;
        this.faculty_id = faculty_id;
    }

    public Abiturient(String first_name, String last_name, float school_grade) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.school_grade = school_grade;
    }

}
