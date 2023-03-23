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

    private static UserDaoImpl instance;

    public static synchronized UserDaoImpl getInstance(){
        if(instance == null){
            instance = new UserDaoImpl();
        }
        return instance;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT id FROM public.user";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultIdSet = statement.executeQuery();

            while(resultIdSet.next()){
                int id = resultIdSet.getInt(1);
                User user = getUserById(id);

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

    public User getUserById(int id){

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
                String user_email = resultSet.getString(4);
                Role user_role = Role.valueOf(resultSet.getString(5));
                String user_cache = resultSet.getString(6);

                user = new User(user_id, user_first_name, user_last_name, user_email, user_role, user_cache);
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }

        return user;
    }

    @Override
    public void createUser(User user) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){


            String query = "INSERT INTO public.user (first_name, last_name, email, role, cache)" +
                    "VALUES (?,?,?,?::role,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, String.valueOf(user.getRole()).toUpperCase());
            statement.setString(5, user.getCache());


            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot create User");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void updateUser(User user) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){

            String query =  "UPDATE public.user " +
                            "SET first_name = ?, last_name = ?, email = ?, role = ?::role, cache = ? " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, String.valueOf(user.getRole()).toUpperCase());
            statement.setString(5, user.getCache());
            statement.setInt(6, user.getId());

            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot update User");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void deleteUser(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "DELETE FROM public.user " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);


            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot delete User");
            }


            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public User getByEmail(String email) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        User user = null;

        try(Connection connection = connectionPool.getConnection()){


            String query =  "SELECT * FROM public.user " +
                            "WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Integer user_id = resultSet.getInt(1);
                String user_first_name = resultSet.getString(2);
                String user_last_name = resultSet.getString(3);
                String user_email = resultSet.getString(4);
                Role user_role = Role.valueOf(resultSet.getString(5));
                String user_cache = resultSet.getString(6);

                user = new User(user_id, user_first_name, user_last_name, user_email, user_role, user_cache);

            }


            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
        return user;
    }
}
