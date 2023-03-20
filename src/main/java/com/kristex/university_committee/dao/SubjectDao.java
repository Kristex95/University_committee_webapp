package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Subject;

import java.util.List;

public interface SubjectDao {

    public Subject getSubjectById(int id);
    public List<Subject> getAllSubjects();
    public void createSubject(Subject subject);
    void updateSubject(Subject subject);
    void deleteSubject(int id);
}
