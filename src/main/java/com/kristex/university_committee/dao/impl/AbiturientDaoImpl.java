package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.AbiturientDao;
import com.kristex.university_committee.model.Abiturient;
import com.kristex.university_committee.model.AbiturientGrades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AbiturientDaoImpl implements AbiturientDao {

    @Override
    public Abiturient GetAbiturientById(int id) {
        Abiturient abiturient = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM abiturient WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                String first_name = resultSet.getString(2);
                String last_name = resultSet.getString(3);
                Float school_grade = resultSet.getFloat(4);
                Integer faculty_id = resultSet.getInt(5);
                abiturient = new Abiturient(first_name, last_name, school_grade, faculty_id);
                System.out.println(abiturient);
            }
            else{
                System.out.println("Abiturient not found");
                return null;
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return abiturient;
    }

    @Override
    public List<Abiturient> GetAllAbiturients() {
        List<Abiturient> abiturientList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT * FROM abiturient";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Integer id = resultSet.getInt(1);
                String first_name = resultSet.getString(2);
                String last_name = resultSet.getString(3);
                Float school_grade = resultSet.getFloat(4);
                Integer faculty = resultSet.getInt(5);

                Abiturient abiturient = new Abiturient(id, first_name, last_name, school_grade, faculty);

                System.out.println(abiturient);
                abiturientList.add(abiturient);
            }
            System.out.println("<End of list>");
            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return abiturientList;
    }

    public AbiturientGrades GetAbiturientGradesById(int id){
        AbiturientGrades abiturientGrades = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String facultyAndAbiturientQuery =  "SELECT abiturient.first_name, abiturient.last_name, abiturient.grade, faculty.name" +
                                                "FROM abiturient" +
                                                "JOIN faculty ON abiturient.faculty_id = faculty.id" +
                                                "WHERE abiturient.id = ?";
            PreparedStatement statement = connection.prepareStatement(facultyAndAbiturientQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                String abit_first_name = resultSet.getString(1);
                String abit_last_name = resultSet.getString(2);
                Float school_grade = resultSet.getFloat(3);
                String faculty = resultSet.getString(4);

                abiturientGrades.setAbiturient(new Abiturient(abit_first_name, abit_last_name,school_grade));
                abiturientGrades.setFaculty(faculty);
            }
            else {
                return null;
            }

            String gradesQuery =    "SELECT result.grade, subject.name, abiturient.first_name, abiturient.last_name" +
                                    "FROM result" +
                                    "JOIN subject ON result.subj_id = subject.id" +
                                    "join abiturient on result.abit_id = abiturient.id" +
                                    "where abiturient.id = ?";
            statement = connection.prepareStatement(gradesQuery);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while(resultSet.next()){
                
            }

        }catch (Exception e){
            System.out.println(e);
        }

        return abiturientGrades;
    }
}
