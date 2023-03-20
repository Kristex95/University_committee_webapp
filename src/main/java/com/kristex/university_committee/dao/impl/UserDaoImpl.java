package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.UserDao;
import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static UserDao instance;

    public static synchronized UserDao getInstance(){
        if(instance == null){
            instance = new UserDaoImpl();
        }
        return instance;
    }

    @Override
    public List<User> GetAllUsers() {
        List<User> userList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT id FROM public.user";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultIdSet = statement.executeQuery();

            while(resultIdSet.next()){
                int id = resultIdSet.getInt(1);
                User user = GetUserById(id);

                System.out.println(user);
                userList.add(user);
            }
            System.out.println("<End of list>");
            resultIdSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return userList;
    }

    public User GetUserById(int id){

        User user = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String facultyAndAbiturientQuery =  "Select * FROM public.user WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(facultyAndAbiturientQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Integer user_id = resultSet.getInt(1);
                String user_first_name = resultSet.getString(2);
                String user_last_name = resultSet.getString(3);
                Integer user_school_grade = resultSet.getInt(4);
                Integer user_faculty_id = resultSet.getInt(5);
                String user_email = resultSet.getString(6);
                Role user_role = Role.valueOf(resultSet.getString(7));
                String user_cache = resultSet.getString(8);
                String user_salt = resultSet.getString(9);

                user = new User(user_id, user_first_name, user_last_name, user_school_grade, user_faculty_id, user_email, user_role, user_cache, user_salt);
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }

        return user;
    }
}
