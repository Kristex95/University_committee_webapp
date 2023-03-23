package com.kristex.university_committee.dao.impl;


import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.FacultyDao;
import com.kristex.university_committee.model.Faculty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FacultyDaoImpl implements FacultyDao {

    private static FacultyDaoImpl instance;

    public static synchronized FacultyDaoImpl getInstance(){
        if(instance == null){
            instance = new FacultyDaoImpl();
        }
        return instance;
    }

    public Faculty getFacultyById(int id){
        Faculty faculty = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM faculty WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Integer facultyId = resultSet.getInt(1);
                String facultyName = resultSet.getString(2);
                faculty = new Faculty(facultyId, facultyName);
            }
            else{
                System.out.println("Faculty not found");
                return null;
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return faculty;
    }

    public List<Faculty> getAllFaculties(){
        List<Faculty> facultyList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM faculty";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                facultyList.add(new Faculty(id, name));
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return facultyList;
    }

    @Override
    public void createFaculty(Faculty faculty) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "INSERT INTO faculty (name) " +
                            "VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, faculty.getName());

            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot create Faculty");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void updateFaculty(Faculty faculty) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "UPDATE faculty " +
                            "SET name = ? " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, faculty.getName());
            statement.setInt(2, faculty.getId());

            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot update Faculty");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void deleteFaculty(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "DELETE FROM faculty " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot delete Faculty");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
