package com.kristex.university_committee.servlets;

import com.kristex.university_committee.model.Registration;
import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.service.RegistrationService;
import com.kristex.university_committee.service.UserService;
import com.kristex.university_committee.utils.JSONParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "RegistrationServlet", value = "/registration/*")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String regIdStr = req.getPathInfo();

        if(regIdStr != null) {

//one registration by id

            int regId = Integer.parseInt(regIdStr.substring(1));

            Registration registration = RegistrationService.getById(regId);
            if(registration ==null){
                out.println("Abiturient not found");
                return;
            }

            //print

            JSONObject userJSON = JSONParser.getRegistrationJSON(registration);

            out.println(userJSON);
        }
        else {

//all abiturients

            List<Registration> registrationList = RegistrationService.getAllList();
            registrationList.sort((o1, o2) -> o1.getId() > o2.getId() ? 1 : -1);

            JSONArray array = new JSONArray();

            for (Registration registration : registrationList) {
                array.put(JSONParser.getRegistrationJSON(registration));
            }
            out.println(array);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        if(pathInfo == null || pathInfo.equals("/")) {
            //create
            Registration registration = new Registration(
                    json.getInt("user_id"),
                    json.getInt("faculty_id"),
                    json.getFloat("school_mark"),
                    json.getInt("math_mark"),
                    json.getInt("history_mark"),
                    json.getInt("english_mark")
            );

            RegistrationService.createRegistration(registration);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        Registration registration = new Registration(
                json.getInt("user_id"),
                json.getInt("faculty_id"),
                json.getFloat("school_mark"),
                json.getInt("math_mark"),
                json.getInt("history_mark"),
                json.getInt("english_mark")
        );


        registration.setId(Integer.parseInt(pathInfo.substring(1)));
        RegistrationService.updateRegistration(registration);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if(pathInfo==null || pathInfo.equals("/")){
            return;
        }
        int id = Integer.parseInt(pathInfo.substring(1));

        RegistrationService.deleteRegistration(id);
    }
}
