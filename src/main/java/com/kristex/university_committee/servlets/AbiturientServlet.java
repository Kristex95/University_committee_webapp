package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.AbiturientDaoImpl;
import com.kristex.university_committee.model.Abiturient;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

            Abiturient abiturient = abiturientDao.GetAbiturientById(Integer.parseInt(abiturientID));
            if(abiturient==null){
                out.println("Abiturient not found");
                return;
            }
            out.println("<p>" + abiturient + "</p>");
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
