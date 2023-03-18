package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Result;

import java.util.List;

public interface ResultDao {

    public Result GetResultById(int id);
    public List<Result> GetAllResults();
}
