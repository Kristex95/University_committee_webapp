package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Subject;

import java.util.List;

public interface SubjectDao {

    public Subject GetSubjectById(int id);
    public List<Subject> GetAllSubjects();
}
