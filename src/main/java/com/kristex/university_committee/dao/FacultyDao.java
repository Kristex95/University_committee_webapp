package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Faculty;

import java.util.List;

public interface FacultyDao {
    public Faculty getFacultyById(int id);
    public List<Faculty> getAllFaculties();
    void createFaculty(Faculty faculty);
    void updateFaculty(Faculty faculty);
    void deleteFaculty(int id);
}
