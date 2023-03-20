package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.SubjectDao;
import com.kristex.university_committee.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoImpl implements SubjectDao {

    private static SubjectDaoImpl instance;

    public static synchronized SubjectDaoImpl getInstance() {
        if (instance == null){
            instance = new SubjectDaoImpl();
        }
        return instance;
    }

    @Override
    public Subject getSubjectById(int id) {

        Subject subject = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            String query = "SELECT name FROM subject WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                String subjectName = resultSet.getString("name");
                subject = new Subject(id, subjectName);
            }
            else{
                System.out.println("Subject not found");
                return null;
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e) {
            System.out.println(e);
        }
        return subject;
    }

    @Override
    public List<Subject> getAllSubjects() {
        List<Subject> subjectList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM subject";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                subjectList.add(new Subject(id, name));
            }
            System.out.println("<End of list>");

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return subjectList;
    }

    public void createSubject(Subject subject){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "INSERT INTO subject (name)" +
                            "VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, subject.getName());
            statement.executeQuery();

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void updateSubject(Subject subject) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "UPDATE subject " +
                            "SET name = ? " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, subject.getName());
            statement.setInt(2, subject.getId());
            statement.executeQuery();

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void deleteSubject(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        Connection connection = connectionPool.getConnection()){
            String query =  "DELETE FROM subject " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeQuery();

            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
