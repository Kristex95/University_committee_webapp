package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.FacultyDaoImpl;
import com.kristex.university_committee.model.Faculty;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/faculty/*")
public class FacultyServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");


        FacultyDaoImpl facultyDao = new FacultyDaoImpl();
        PrintWriter out = response.getWriter();

        String facultyID = request.getPathInfo();
        if(facultyID != null) {
            // /1
            facultyID = facultyID.substring(1);

            Faculty faculty = facultyDao.GetFacultyById(Integer.parseInt(facultyID));
            if(faculty==null){
                out.println("Faculty not found");
                return;
            }
            out.println("<p>" + faculty.getId() + ": " + faculty.getName() + "</p>");
        }
        else {
            List<Faculty> facultyList = facultyDao.GetAllFaculties();
            for (Faculty faculty: facultyList) {
                out.println("<p>" + faculty + "</p>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    public void destroy() {
    }
}