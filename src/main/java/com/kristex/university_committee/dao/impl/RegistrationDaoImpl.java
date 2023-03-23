package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.RegistrationDao;
import com.kristex.university_committee.model.Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDaoImpl implements RegistrationDao {

    private static RegistrationDaoImpl instance;

    public static synchronized RegistrationDaoImpl getInstance(){
        if(instance == null){
            instance = new RegistrationDaoImpl();
        }
        return instance;
    }

    @Override
    public Registration getById(int id) {
        Registration registration = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String facultyAndAbiturientQuery =  "Select * FROM registration WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(facultyAndAbiturientQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                int user_id = resultSet.getInt(2);
                int faculty_id = resultSet.getInt(3);
                float school_mark = resultSet.getFloat(4);
                int math_mark = resultSet.getInt(5);
                int history_mark = resultSet.getInt(6);
                int english_mark = resultSet.getInt(7);

                registration = new Registration(id, user_id, faculty_id, school_mark, math_mark, history_mark,english_mark);
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }

        return registration;
    }

    @Override
    public List<Registration> getAllList() {
        List<Registration> registrationList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT id FROM registration";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultIdSet = statement.executeQuery();

            while(resultIdSet.next()){
                int id = resultIdSet.getInt(1);
                Registration registration = getById(id);

                System.out.println(registration);
                registrationList.add(registration);
            }
            System.out.println("<End of list>");
            resultIdSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return registrationList;
    }

    @Override
    public void createRegistration(Registration registration) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){


            String query =  "INSERT INTO registration (user_id, faculty_id, school_mark, math_mark, history_mark, english_mark) " +
                            "VALUES (?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, registration.getUserId());
            statement.setInt(2, registration.getFacultyId());
            statement.setFloat(3, registration.getSchoolMark());
            statement.setInt(4, registration.getMathMark());
            statement.setInt(5, registration.getHistoryMark());
            statement.setInt(6, registration.getEnglishMark());


            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot create Registration");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void updateRegistration(Registration registration) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){

            String query =  "UPDATE registration " +
                            "SET user_id = ?, faculty_id = ?, school_mark = ?, math_mark = ?, history_mark = ?, english_mark = ? " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, registration.getUserId());
            statement.setInt(2, registration.getFacultyId());
            statement.setFloat(3, registration.getSchoolMark());
            statement.setInt(4, registration.getMathMark());
            statement.setInt(5, registration.getHistoryMark());
            statement.setInt(6, registration.getEnglishMark());
            statement.setInt(7, registration.getId());

            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot update Registration");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void deleteRegistration(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "DELETE FROM registration " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);


            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot delete Registration");
            }


            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
