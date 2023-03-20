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

    public static Result getResultById(int id){
        return ResultDaoImpl.getInstance().GetResultById(id);
    }

    public static List<Result> getAllResults(){
        return ResultDaoImpl.getInstance().GetAllResults();
    }
    public static void createResult(Result result){
        ResultDaoImpl.getInstance().createResult(result);
    }
    public static void updateResult(Result result){
        ResultDaoImpl.getInstance().updateResult(result);
    }
    public static void deleteResult(int id){
        ResultDaoImpl.getInstance().deleteResult(id);
    }
}
