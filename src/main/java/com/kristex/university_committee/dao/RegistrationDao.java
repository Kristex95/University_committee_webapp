package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Registration;

import java.util.List;

public interface RegistrationDao {
    Registration getById(int id);
    List<Registration> getAllList();
    void createRegistration(Registration registration);
    void updateRegistration(Registration registration);
    void deleteRegistration(int id);
}
