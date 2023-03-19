package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.AbiturientDao;
import com.kristex.university_committee.model.Abiturient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                abiturient = new Abiturient(first_name, last_name, school_grade);
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
                String first_name = resultSet.getString(2);
                String last_name = resultSet.getString(3);
                Float school_grade = resultSet.getFloat(4);

                Abiturient abiturient = new Abiturient(first_name, last_name, school_grade);

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

    public Abiturient GetAbiturientGradesAndFacultyById(int id){
        Abiturient abiturient = GetAbiturientById(id);

        String faculty;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String facultyAndAbiturientQuery =  "SELECT faculty.name " +
                                                "FROM abiturient " +
                                                "JOIN faculty ON abiturient.faculty_id = faculty.id " +
                                                "WHERE abiturient.id = ? ";
            PreparedStatement statement = connection.prepareStatement(facultyAndAbiturientQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                faculty = resultSet.getString(1);

                abiturient.setFaculty(faculty);
            }
            else {
                return null;
            }

            String gradesQuery =    "SELECT s.name, res.grade " +
                                    "FROM result res " +
                                    "INNER JOIN subject s ON res.subj_id = s.id " +
                                    "INNER JOIN abiturient abit on res.abit_id = abit.id " +
                                    "WHERE abit.id = ?";
            statement = connection.prepareStatement(gradesQuery);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();


            Map<String, Integer> grades = new HashMap<>();
            while(resultSet.next()){
                grades.put(resultSet.getString(1), resultSet.getInt(2));
            }

            abiturient.setCertificate_grades(grades);

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }

        return abiturient;
    }
}
