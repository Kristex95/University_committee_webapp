package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.FacultyDaoImpl;
import com.kristex.university_committee.model.Faculty;

import java.util.List;

public class FacultyService {

    private static FacultyService instance;

    public static synchronized FacultyService getInstance(){
        if(instance == null){
            instance = new FacultyService();
        }
        return instance;
    }

    public static Faculty GetFacultyById(int id){
        return FacultyDaoImpl.getInstance().getFacultyById(id);
    }

    public static List<Faculty> GetAllFaculties(){
        return FacultyDaoImpl.getInstance().getAllFaculties();
    }

    public static void createFaculty(Faculty faculty){
        FacultyDaoImpl.getInstance().createFaculty(faculty);
    }

    public static void updateFaculty(Faculty faculty){
        FacultyDaoImpl.getInstance().updateFaculty(faculty);
    }

    public static void deleteFaculty(int id){
        FacultyDaoImpl.getInstance().deleteFaculty(id);
    }

}
