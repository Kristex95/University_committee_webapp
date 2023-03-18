package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.ResultDao;
import com.kristex.university_committee.model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResultDaoImpl implements ResultDao {

    @Override
    public Result GetResultById(int id) {
        Result result = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM result WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Integer abit_id = resultSet.getInt(2);
                Integer subj_id = resultSet.getInt(3);
                Float grade = resultSet.getFloat(4);
                result = new Result(id, abit_id, subj_id, grade);
            }
            else{
                System.out.println("Result not found");
                return null;
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    @Override
    public List<Result> GetAllResults() {
        List<Result> resultList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM result";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                Integer abit_id = resultSet.getInt(2);
                Integer subj_id = resultSet.getInt(3);
                Float grade = resultSet.getFloat(4);

                Result result = new Result(id, abit_id, subj_id, grade);
                System.out.println(result);
                resultList.add(result);
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }

        return resultList;
    }
}
