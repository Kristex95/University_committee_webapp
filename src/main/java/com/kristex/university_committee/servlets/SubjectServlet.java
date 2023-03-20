package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.SubjectDaoImpl;
import com.kristex.university_committee.model.Faculty;
import com.kristex.university_committee.model.Subject;
import com.kristex.university_committee.service.FacultyService;
import com.kristex.university_committee.service.SubjectService;
import com.kristex.university_committee.utils.JSONParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        if(pathInfo == null || pathInfo.equals("/")) { //create
            Subject newSubject = new Subject(json.getString("name"));
            SubjectService.createSubject(newSubject);
        }
        else {  //update
            Integer id = Integer.valueOf(pathInfo.substring(1));
            Subject updatedSubject = new Subject(id,json.getString("name"));

            SubjectService.updateSubject(updatedSubject);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();

        if(pathInfo==null || pathInfo.equals("/")){
            return;
        }

        Integer id = Integer.valueOf(pathInfo.substring(1));

        SubjectService.deleteSubject(id);
    }
}
