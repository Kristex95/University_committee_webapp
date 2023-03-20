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

    private static ResultDaoImpl instance;

    public static synchronized ResultDaoImpl getInstance(){
        if(instance == null){
            instance = new ResultDaoImpl();
        }
        return instance;
    }

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

    @Override
    public void createResult(Result result) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            String query =  "INSERT INTO result (abit_id, subj_id, grade)" +
                            "VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, result.getAbitId());
            statement.setInt(2, result.getSubjId());
            statement.setFloat(3, result.getGrade());
            statement.executeQuery();

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void updateResult(Result result) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            String query =  "UPDATE result " +
                            "SET abit_id = ?, subj_id = ?, grade = ? " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, result.getAbitId());
            statement.setInt(2, result.getSubjId());
            statement.setFloat(3, result.getGrade());
            statement.setInt(4, result.getId());
            statement.executeQuery();

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void deleteResult(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            String query =  "DELETE FROM result " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeQuery();

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
