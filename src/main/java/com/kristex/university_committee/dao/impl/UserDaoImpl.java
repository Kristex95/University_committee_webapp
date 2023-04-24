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

    public static synchronized UserDaoImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            String query = "SELECT id FROM public.user";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultIdSet = statement.executeQuery();

            while (resultIdSet.next()) {
                int id = resultIdSet.getInt(1);
                User user = getUserById(id);

                userList.add(user);
            }
            resultIdSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return userList;
    }

    @Override
    public List<User> getAllUsersByFacultyId(int faculty_id) {
        List<User> userList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            String query = "SELECT public.user.id, public.user.first_name, public.user.last_name FROM faculty " +
                    "INNER JOIN registration reg on reg.faculty_id = faculty.id " +
                    "INNER JOIN public.user on public.user.id = reg.user_id " +
                    "WHERE faculty.id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, faculty_id);
            ResultSet resultIdSet = statement.executeQuery();

            while (resultIdSet.next()) {
                int user_id = resultIdSet.getInt(1);
                User user = getUserById(user_id);

                userList.add(user);
            }
            resultIdSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return userList;
    }

    @Override
    public List<User> getAllUnconfirmedUsers() {
        List<User> userList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            String query = "SELECT * FROM public.user " +
                    "WHERE confirmed = false and role = 'ABITURIENT'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultIdSet = statement.executeQuery();

            while (resultIdSet.next()) {
                int user_id = resultIdSet.getInt(1);
                User user = getUserById(user_id);

                userList.add(user);
            }
            resultIdSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return userList;
    }

    public User getUserById(int id) {

        User user = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            String facultyAndAbiturientQuery = "Select * FROM public.user WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(facultyAndAbiturientQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Integer user_id = resultSet.getInt(1);
                String first_name = resultSet.getString(2);
                String last_name = resultSet.getString(3);
                String user_email = resultSet.getString(4);
                Role role = Role.valueOf(resultSet.getString(5));
                float school_mark = resultSet.getFloat(6);
                int math_mark = resultSet.getInt(7);
                int english_mark = resultSet.getInt(8);
                int history_mark = resultSet.getInt(9);
                String cache = resultSet.getString(10);
                Boolean confirmed = resultSet.getBoolean(11);

                user = new User(user_id, first_name, last_name, user_email, role, school_mark, math_mark, english_mark, history_mark, cache, confirmed);
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }

    @Override
    public void createUser(User user) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {


            String query = "INSERT INTO public.user (first_name, last_name, email, school_mark, math_mark, english_mark, history_mark, cache)" +
                    "VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setFloat(4, user.getSchool_mark());
            statement.setInt(5, user.getMath_mark());
            statement.setInt(6, user.getEnglish_mark());
            statement.setInt(7, user.getHistory_mark());
            statement.setString(8, user.getCache());


            if (statement.executeUpdate() <= 0) {
                System.out.println("SQL cannot create User");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void updateUser(User user) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {

            String query = "UPDATE public.user " +
                    "SET first_name = ?, last_name = ?, email = ?, school_mark = ?, math_mark = ?, english_mark = ?, history_mark = ?, confirmed = ? " +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setFloat(4, user.getSchool_mark());
            statement.setInt(5, user.getMath_mark());
            statement.setInt(6, user.getEnglish_mark());
            statement.setInt(7, user.getHistory_mark());
            statement.setBoolean(8, user.isConfirmed());
            statement.setInt(9, user.getId());

            if (statement.executeUpdate() <= 0) {
                System.out.println("SQL cannot update User");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void deleteUser(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            String query = "DELETE FROM public.user " +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);


            if (statement.executeUpdate() <= 0) {
                System.out.println("SQL cannot delete User");
            }


            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public User getByEmail(String email) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        User user = null;

        try (Connection connection = connectionPool.getConnection()) {


            String query = "SELECT * FROM public.user " +
                    "WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Integer user_id = resultSet.getInt(1);
                String first_name = resultSet.getString(2);
                String last_name = resultSet.getString(3);
                String user_email = resultSet.getString(4);
                Role role = Role.valueOf(resultSet.getString(5));
                float school_mark = resultSet.getFloat(6);
                int math_mark = resultSet.getInt(7);
                int english_mark = resultSet.getInt(8);
                int history_mark = resultSet.getInt(9);
                String cache = resultSet.getString(10);
                Boolean confirmed = resultSet.getBoolean(11);

                user = new User(user_id, first_name, last_name, user_email, role, school_mark, math_mark, english_mark, history_mark, cache, confirmed);

            }


            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return user;
    }

    @Override
    public void confirmUser(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            String query =  "UPDATE public.user " +
                            "SET confirmed = true " +
                            "WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);


            if (statement.executeUpdate() <= 0) {
                System.out.println("SQL cannot confirm User");
            }


            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);

        }
    }
}
