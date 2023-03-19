package com.kristex.university_committee.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kristex.university_committee.dao.impl.AbiturientDaoImpl;
import com.kristex.university_committee.model.Abiturient;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/abiturient/*")
public class AbiturientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");


        AbiturientDaoImpl abiturientDao = new AbiturientDaoImpl();
        PrintWriter out = resp.getWriter();

        String abiturientID = req.getPathInfo();
        if(abiturientID != null) {

//one abiturient by id

            abiturientID = abiturientID.substring(1);

//            Abiturient abiturient = abiturientDao.GetAbiturientById(Integer.parseInt(abiturientID));
            Abiturient abiturient = abiturientDao.GetAbiturientGradesAndFacultyById(Integer.parseInt(abiturientID));
            if(abiturient==null){
                out.println("Abiturient not found");
                return;
            }

            //print

            out.println("<p>Abiturient: " + abiturient.getFirst_name() + " " + abiturient.getLast_name() + "</p>");
            out.println("<p>Faculty: " + abiturient.getFaculty() + "</p>");
            out.println("<p>School grade: " + abiturient.getSchool_grade() + "</p>");
            out.println("<p>Certificate grades: </p>");
            for(Map.Entry<String, Integer> grade : abiturient.getCertificate_grades().entrySet()){
                out.println("<p>" + grade.getKey() + ": " + grade.getValue() + "</p>");
            }


            out.println("<code>" + abiturient.GetJSON() + "</code>");
        }
        else {

//all abiturients

            List<Abiturient> abiturientList = abiturientDao.GetAllAbiturients();
            abiturientList.sort((o1, o2) -> o1.getId() > o2.getId() ? 1 : -1);
            for (Abiturient abiturient: abiturientList) {
                out.println("<p>" + abiturient + "</p>");
            }
        }
    }
}
