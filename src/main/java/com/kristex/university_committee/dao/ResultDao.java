package com.kristex.university_committee.dao;

import com.kristex.university_committee.model.Result;

import java.util.List;

public interface ResultDao {

    Result GetResultById(int id);
    List<Result> GetAllResults();
    void createResult(Result result);
    void updateResult(Result result);
    void deleteResult(int id);
}
