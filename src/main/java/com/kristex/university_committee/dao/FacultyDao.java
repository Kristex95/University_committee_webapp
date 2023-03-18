package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Faculty;

import java.util.List;

public interface FacultyDao {
    public Faculty GetFacultyById(int id);
    public List<Faculty> GetAllFaculties();
}
