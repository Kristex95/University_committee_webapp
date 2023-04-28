package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.RegistrationDao;
import com.kristex.university_committee.model.Registration;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDaoImpl implements RegistrationDao {
    private static final Logger log = Logger.getLogger(RegistrationDaoImpl.class);
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


                registration = new Registration(id, user_id, faculty_id);
            }
            else {
                log.error("DB: got registration by id");
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            log.error("DB: " + e);
        }

        return registration;
    }

    @Override
    public Registration getByUserId(int id){
        Registration registration = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String facultyAndAbiturientQuery =  "Select * FROM registration WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(facultyAndAbiturientQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                int registration_id = resultSet.getInt(1);
                int faculty_id = resultSet.getInt(3);


                registration = new Registration(registration_id, id, faculty_id);
            }
            else{
                log.error("DB: could not find registration by user id");
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            log.error("DB: " + e);
            return null;
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

            if(!resultIdSet.next()){
                log.error("DB: could get list of faculties by user id");
            }
            else {
                do{
                    int id = resultIdSet.getInt(1);
                    Registration registration = getById(id);

                    System.out.println(registration);
                    registrationList.add(registration);
                }while (resultIdSet.next());
            }
            resultIdSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            log.error("DB: " + e);
        }

        return registrationList;
    }

    @Override
    public void createRegistration(Registration registration) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){


            String query =  "INSERT INTO registration (user_id, faculty_id) " +
                            "VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, registration.getUserId());
            statement.setInt(2, registration.getFacultyId());



            if (statement.executeUpdate() <= 0){
                log.error("DB: could not create registration");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            log.error("DB: " + e);
        }
    }

    @Override
    public void updateRegistration(Registration registration) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){

            String query =  "UPDATE registration " +
                            "SET user_id = ?, faculty_id = ? " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, registration.getUserId());
            statement.setInt(2, registration.getFacultyId());
            statement.setInt(3, registration.getId());

            if (statement.executeUpdate() <= 0){
                log.error("DB: could not update registration");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            log.error("DB: " + e);
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
                log.error("DB: could not delete registration");
            }


            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            log.error("DB: " + e);
        }
    }
}
