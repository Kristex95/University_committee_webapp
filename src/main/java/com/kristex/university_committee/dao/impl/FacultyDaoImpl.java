package com.kristex.university_committee.dao.impl;


import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.FacultyDao;
import com.kristex.university_committee.model.Faculty;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FacultyDaoImpl implements FacultyDao {
    private static final Logger log = Logger.getLogger(FacultyDaoImpl.class);

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
                log.error("DB: Faculty by id was not found");
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            log.error("DB: " + e);
            return null;
        }

        return faculty;
    }

    public List<Faculty> getAllFacultiesByUserId(int id){
        List<Faculty> facultyList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "SELECT faculty_id, name FROM registration\n" +
                            "INNER JOIN faculty on registration.faculty_id = faculty.id\n" +
                            "WHERE registration.user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                log.error("DB: could not get list of faculties by user id");
            }
            else {
                do{
                    int faculty_id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    facultyList.add(new Faculty(faculty_id, name));
                }while (resultSet.next());
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            log.error("DB: " + e);
            return null;
        }

        return facultyList;
    }

    public List<Faculty> getAllFaculties(){
        List<Faculty> facultyList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM faculty";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                log.error("DB: could get list of faculties");
            }
            else {
                do {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    facultyList.add(new Faculty(id, name));
                } while (resultSet.next());
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            log.error("DB: could not list of faculties");
            return null;
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
                log.error("DB: could not create faculty");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            log.error("DB: " + e);
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
                statement.close();
                connectionPool.releaseConnection(connection);
                log.error("DB: could not update faculty");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            log.error("DB: " + e);
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
                statement.close();
                connectionPool.releaseConnection(connection);
                log.error("DB: could not delete faculty");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            log.error("DB: " + e);
        }
    }
}
