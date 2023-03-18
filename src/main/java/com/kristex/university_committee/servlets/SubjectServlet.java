package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.FacultyDaoImpl;
import com.kristex.university_committee.dao.impl.SubjectDaoImpl;
import com.kristex.university_committee.model.Faculty;
import com.kristex.university_committee.model.Subject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/subject/*")
public class SubjectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");


        SubjectDaoImpl subjectDao = new SubjectDaoImpl();
        PrintWriter out = resp.getWriter();

        String subjectID = req.getPathInfo();
        if(subjectID != null) {
            subjectID = subjectID.substring(1);

            Subject subject = subjectDao.GetSubjectById(Integer.valueOf(subjectID));
            if(subject==null){
                out.println("Faculty not found");
                return;
            }
            out.println("<p>" + subject + "</p>");
        }
        else {
            List<Subject> subjectList = subjectDao.GetAllSubjects();
            for (Subject subject: subjectList) {
                out.println("<p>" + subject + "</p>");
            }
        }
    }
}
