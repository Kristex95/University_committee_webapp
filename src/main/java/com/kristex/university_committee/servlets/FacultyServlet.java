package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.FacultyDaoImpl;
import com.kristex.university_committee.model.Faculty;
import com.kristex.university_committee.service.FacultyService;
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

@WebServlet("/faculty/*")
public class FacultyServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");


        FacultyDaoImpl facultyDao = new FacultyDaoImpl();
        PrintWriter out = response.getWriter();

        String facultyID = request.getPathInfo();
        if(facultyID != null) {

    //By id

            facultyID = facultyID.substring(1);

            Faculty faculty = FacultyService.getInstance().GetFacultyById(Integer.valueOf(facultyID));
            if(faculty==null){
                out.println("Faculty not found");
                return;
            }
            out.println(JSONParser.getInstance().getFacultyJSON(faculty));
        }
        else {

    //All

            List<Faculty> facultyList = FacultyService.getInstance().GetAllFaculties();
            JSONArray jsonArray = new JSONArray();
            for (Faculty faculty: facultyList) {
                jsonArray.put(JSONParser.getInstance().getFacultyJSON(faculty));
            }
            out.println(jsonArray);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        if(pathInfo == null || pathInfo.equals("/")) { //create
            Faculty newFaculty = new Faculty(json.getString("name"));
            FacultyService.createFaculty(newFaculty);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        Integer id = Integer.valueOf(pathInfo.substring(1));
        Faculty updatedFaculty = new Faculty(id,json.getString("name"));

        FacultyService.updateFaculty(updatedFaculty);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();

        if(pathInfo==null || pathInfo.equals("/")){
            return;
        }

        Integer id = Integer.valueOf(pathInfo.substring(1));

        FacultyService.deleteFaculty(id);
    }

    public void destroy() {
    }
}