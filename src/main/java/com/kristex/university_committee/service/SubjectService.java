package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.SubjectDaoImpl;
import com.kristex.university_committee.model.Subject;

import java.util.List;

public class SubjectService {
    private static SubjectService instance;

    public static synchronized SubjectService getInstance() {
        if(instance == null){
            instance = new SubjectService();
        }
        return instance;
    }

    public static Subject GetSubjectById(int id){
        return SubjectDaoImpl.getInstance().getSubjectById(id);
    }

    public static List<Subject> GetAllSubjects(){
        return SubjectDaoImpl.getInstance().getAllSubjects();
    }

    public static void createSubject(Subject subject){
        SubjectDaoImpl.getInstance().createSubject(subject);
    }

    public static void updateSubject(Subject subject){
        SubjectDaoImpl.getInstance().updateSubject(subject);
    }

    public static void deleteSubject(int id){
        SubjectDaoImpl.getInstance().deleteSubject(id);
    }
}
