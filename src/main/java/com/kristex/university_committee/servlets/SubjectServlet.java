package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.SubjectDaoImpl;
import com.kristex.university_committee.model.Subject;
import com.kristex.university_committee.service.SubjectService;
import com.kristex.university_committee.utils.JSONParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/subject/*")
public class SubjectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");


        SubjectDaoImpl subjectDao = new SubjectDaoImpl();
        PrintWriter out = resp.getWriter();

        String subjectID = req.getPathInfo();
        if(subjectID != null) {

    //by id

            subjectID = subjectID.substring(1);

            Subject subject = SubjectService.getInstance().GetSubjectById(Integer.valueOf(subjectID));
            if(subject==null){
                out.println("Faculty not found");
                return;
            }
            out.println(JSONParser.getInstance().getSubjectJSON(subject));
        }
        else {

    //all

            List<Subject> subjectList = SubjectService.getInstance().GetAllSubjects();
            JSONArray jsonArray = new JSONArray();
            for (Subject subject: subjectList) {
                jsonArray.put(JSONParser.getInstance().getSubjectJSON(subject));
            }

            out.println(jsonArray);
        }
    }
}
