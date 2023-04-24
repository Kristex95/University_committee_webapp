package com.kristex.university_committee.servlets;

import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.service.UserService;
import com.kristex.university_committee.utils.JSONParser;
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

    private final String FACULTY_USERS_SUBPATH = "/faculty/";
    private final String UNCONFIRMED_USERS_SUBPATH = "/unconfirmed/";
    private final String CONFIRM_USERS_SUBPATH = "/confirm/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();
        JSONObject params = (JSONObject) req.getAttribute("params");
        if(pathInfo != null) {

            if(pathInfo.contains(FACULTY_USERS_SUBPATH)) {
                //All users by faculty id
                String faculty_id = pathInfo.substring(FACULTY_USERS_SUBPATH.length());

                List<User> userList = UserService.getInstance().getAllUsersByFacultyId(Integer.parseInt(faculty_id));

                if (userList.isEmpty()) {
                    System.out.println("No users");
                    return;
                }


                JSONArray jsonArray = new JSONArray();
                for (User user : userList) {
                    jsonArray.put(JSONParser.getUserSimpleJSON(user));
                }
                out.println(jsonArray);

            } else if (pathInfo.contains(UNCONFIRMED_USERS_SUBPATH)) {

                List<User> userList = UserService.getInstance().getAllUnconfirmedUsers();

                if (userList.isEmpty()) {
                    System.out.println("No users");
                    return;
                }

                JSONArray jsonArray = new JSONArray();
                for (User user : userList) {
                    jsonArray.put(JSONParser.getUserJSON(user));
                }
                out.println(jsonArray);

            } else if(pathInfo.contains("/client")){
                //returns logged in user info
                User user = UserService.getInstance().getUserById(params.getInt("id"));
                if (user == null) {
                    out.println("Abiturient not found");
                    return;
                }

                JSONObject userJSON = JSONParser.getUserWithFacultyJSON(user);

                out.println(userJSON);
            }
            else {
                //one abiturient by id


                User user = UserService.getInstance().getUserById(Integer.valueOf(pathInfo.substring(1)));
                if (user == null) {
                    out.println("Abiturient not found");
                    return;
                }

                //print

                JSONObject userJSON = JSONParser.getUserWithFacultyJSON(user);

                out.println(userJSON);
            }

        }
        else {

//all abiturients

            List<User> userList = UserService.getInstance().getAllUser();
            userList.sort((o1, o2) -> o1.getId() > o2.getId() ? 1 : -1);

            JSONArray array = new JSONArray();

            for (User user : userList) {
                array.put(JSONParser.getUserWithFacultyJSON(user));
            }
            out.println(array);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        JSONObject params = (JSONObject) req.getAttribute("params");

        if(pathInfo.equals("/")) {
        //create
            JSONObject json = JSONParser.parseJSON(req);
            User user = new User(
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("email"),
                    UserService.hashPassword(json.getString("pass")));
            UserService.createUser(user);
        }
        else if (pathInfo.contains(CONFIRM_USERS_SUBPATH)) {
            JSONObject json = JSONParser.parseJSON(req);
            UserService.confirmUser(json.getInt("id"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //update
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        JSONObject json = JSONParser.parseJSON(req);
        JSONObject params = (JSONObject) req.getAttribute("params");
        User newUser = JSONParser.parseUserJSON(json);

        User oldUser = UserService.getInstance().getUserById(params.getInt("id"));

        if(oldUser.getSchool_mark() != newUser.getSchool_mark() || oldUser.getMath_mark() != newUser.getMath_mark() || oldUser.getEnglish_mark() != newUser.getEnglish_mark() || oldUser.getHistory_mark() != newUser.getHistory_mark()){
            newUser.setConfirmed(false);
        }

        newUser.setId(params.getInt("id"));
        UserService.updateUser(newUser);
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
