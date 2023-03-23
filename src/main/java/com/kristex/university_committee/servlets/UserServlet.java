package com.kristex.university_committee.servlets;

import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.service.UserService;
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

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String abiturientID = req.getPathInfo();

        if(abiturientID != null) {

//one abiturient by id

            abiturientID = abiturientID.substring(1);

            User user = UserService.getInstance().getUserById(Integer.parseInt(abiturientID));
            if(user ==null){
                out.println("Abiturient not found");
                return;
            }

            //print

            JSONObject userJSON = JSONParser.getUserJSON(user);

            out.println(userJSON);
        }
        else {

//all abiturients

            List<User> userList = UserService.getInstance().getAllUser();
            userList.sort((o1, o2) -> o1.getId() > o2.getId() ? 1 : -1);

            JSONArray array = new JSONArray();

            for (User user : userList) {
                array.put(JSONParser.getUserJSON(user));
            }
            out.println(array);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        if(pathInfo == null || pathInfo.equals("/")) {
        //create
            User user = new User(
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("email"),
                    Role.valueOf(json.getString("role")),
                    UserService.hashPassword(json.getString("pass")));
            UserService.createUser(user);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        User user = new User(
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("email"),
                    Role.valueOf(json.getString("role")),
                    UserService.hashPassword(json.getString("pass")));


        user.setId(Integer.parseInt(pathInfo.substring(1)));
        UserService.updateUser(user);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if(pathInfo==null || pathInfo.equals("/")){
            return;
        }
        int id = Integer.parseInt(pathInfo.substring(1));

        UserService.deleteUser(id);
    }
}
