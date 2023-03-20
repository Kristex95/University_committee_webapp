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

    public Subject GetSubjectById(int id){
        return SubjectDaoImpl.getInstance().GetSubjectById(id);
    }

    public List<Subject> GetAllSubjects(){
        return SubjectDaoImpl.getInstance().GetAllSubjects();
    }
}
