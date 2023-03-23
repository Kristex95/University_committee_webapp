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
    float schoolMark;
    int mathMark;
    int historyMark;
    int englishMark;

    public Registration(int userId, int facultyId, float schoolMark, int mathMark, int historyMark, int englishMark){
        this.userId = userId;
        this.facultyId = facultyId;
        this.schoolMark = schoolMark;
        this.mathMark = mathMark;
        this.historyMark = historyMark;
        this.englishMark = englishMark;
    }
}
