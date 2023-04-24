package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.RegistrationDaoImpl;
import com.kristex.university_committee.model.Registration;

import java.util.List;

public class RegistrationService {

    private static RegistrationService instance;

    public static synchronized RegistrationService getInstance(){
        if(instance == null){
            instance = new RegistrationService();
        }
        return instance;
    }

    public static Registration getById(int id){
        return RegistrationDaoImpl.getInstance().getById(id);
    }
    public static Registration getByUserId(int id) {return RegistrationDaoImpl.getInstance().getByUserId(id);}

    public static List<Registration> getAllList() {
        return RegistrationDaoImpl.getInstance().getAllList();
    }

    public static void createRegistration(Registration registration){
        RegistrationDaoImpl.getInstance().createRegistration(registration);
    }

    public static void updateRegistration(Registration registration){
        RegistrationDaoImpl.getInstance().updateRegistration(registration);
    }

    public static void deleteRegistration(int id){
        RegistrationDaoImpl.getInstance().deleteRegistration(id);
    }
}
