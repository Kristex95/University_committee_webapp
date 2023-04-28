package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.FacultyDaoImpl;
import com.kristex.university_committee.model.Faculty;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.service.FacultyService;
import com.kristex.university_committee.service.UserService;
import com.kristex.university_committee.utils.JSONParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/faculty/*")
public class FacultyServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(FacultyServlet.class);

    private final String FACULTY_USERS_SUBPATH = "/users/";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");


        FacultyDaoImpl facultyDao = new FacultyDaoImpl();
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if(pathInfo != null) {

            //By id

            pathInfo = pathInfo.substring(1);

            Faculty faculty = FacultyService.GetFacultyById(Integer.parseInt(pathInfo));
            if(faculty==null){
                out.println("Faculty not found");
                log.error("Faculty not found");
                return;
            }
            out.println(JSONParser.getFacultyJSON(faculty));
        }
        else {

    //All

            List<Faculty> facultyList = FacultyService.GetAllFaculties();
            if(facultyList.isEmpty()){
                return;
            }
            JSONArray jsonArray = new JSONArray();
            for (Faculty faculty: facultyList) {
                jsonArray.put(JSONParser.getFacultyJSON(faculty));
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
        }else{
            log.error("Faculty was not created");
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
        log.info("Faculty" + updatedFaculty.toString() + " was not created");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        Integer id = json.getInt("faculty_id");
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        FacultyService.deleteFaculty(id);
    }
}