package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Abiturient;

import java.util.List;

public interface AbiturientDao {

    public Abiturient GetAbiturientById(int id);

    public List<Abiturient> GetAllAbiturients();
}
