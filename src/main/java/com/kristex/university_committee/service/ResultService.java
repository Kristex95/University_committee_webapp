package com.kristex.university_committee.service;

import com.kristex.university_committee.dao.impl.ResultDaoImpl;
import com.kristex.university_committee.model.Result;

import java.util.List;

public class ResultService {

    private static ResultService instance;

    public static synchronized ResultService getInstance(){
        if (instance == null){
            instance = new ResultService();
        }
        return instance;
    }

    public Result GetResultById(int id){
        return ResultDaoImpl.getInstance().GetResultById(id);
    }

    public List<Result> GetAllResults(){
        return ResultDaoImpl.getInstance().GetAllResults();
    }
}
