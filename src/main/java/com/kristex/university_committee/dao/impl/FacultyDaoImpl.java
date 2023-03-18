package com.kristex.university_committee.dao.impl;


import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.FacultyDao;
import com.kristex.university_committee.model.Faculty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacultyDaoImpl implements FacultyDao {

    public Faculty GetFacultyById(int id){
        Faculty faculty = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT name FROM faculty WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                String facultyName = resultSet.getString("name");
                System.out.println("Faculty name: " + facultyName);
                faculty = new Faculty(id, facultyName);
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

    public List<Faculty> GetAllFaculties(){
        List<Faculty> facultyList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM faculty";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                System.out.println(id + ": " + name);
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
}
