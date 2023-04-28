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
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "RegistrationServlet", value = "/registration/*")
public class RegistrationServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(RegistrationServlet.class);
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
                out.println("Registration not found");
                log.error("Registration was not found");
                return;
            }

            //print

            JSONObject userJSON = JSONParser.getRegistrationJSON(registration);

            out.println(userJSON);
        }
        else {

//all Registrations

            List<Registration> registrationList = RegistrationService.getAllList();
            if(registrationList.isEmpty()){
                log.error("Registration list is empty");
                return;
            }
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
        JSONObject paramsJSON = (JSONObject) req.getAttribute("params");
        if(pathInfo == null || pathInfo.equals("/")) {
            //create
            Registration registration = new Registration(
                    paramsJSON.getInt("id"),
                    json.getInt("faculty_id")
            );

            RegistrationService.createRegistration(registration);
        }
        else {
            log.error("Could not create registration");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        Registration registration = new Registration(
                json.getInt("user_id"),
                json.getInt("faculty_id")
        );


        registration.setId(Integer.parseInt(pathInfo.substring(1)));
        RegistrationService.updateRegistration(registration);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if(pathInfo==null || pathInfo.equals("/")){
            log.error("Could not delete registration");
            return;
        }
        int id = Integer.parseInt(pathInfo.substring(1));

        RegistrationService.deleteRegistration(id);
    }
}
