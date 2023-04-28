package com.kristex.university_committee.servlets;

import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.service.UserService;
import com.kristex.university_committee.utils.JSONParser;
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

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(UserService.class);

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

                JSONArray usersJSON = UserService.getInstance().getAcceptedUsersJSON(Integer.parseInt(faculty_id));
                System.out.println(usersJSON);
                if (usersJSON.length() == 0) {
                    System.out.println("No users");
                    log.error("Users by faculty id " + faculty_id + " were not found");
                    return;
                }

                out.println(usersJSON);

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
                if(params == null){
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                User user = UserService.getInstance().getUserById(params.getInt("id"));
                if (user == null) {
                    out.println("Abiturient not found");
                    log.error("This user not found");
                    return;
                }

                JSONObject userJSON = JSONParser.getUserWithFacultyJSON(user);

                out.println(userJSON);
            }
            else {
                //one abiturient by id

                int id = Integer.parseInt(pathInfo.substring(1));
                User user = UserService.getInstance().getUserById(id);
                if (user == null) {
                    out.println("Abiturient not found");
                    log.error("Specific user with id: " + id + " not found");
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
            if(userList.isEmpty()){
                log.error("User list is empty");
                return;
            }
            userList.sort((o1, o2) -> o1.getId() > o2.getId() ? 1 : -1);

            JSONArray array = new JSONArray();
            for (User user : userList) {
                array.put(JSONParser.getUserSimpleJSON(user));
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
            return;
        }
        else if (pathInfo.contains(CONFIRM_USERS_SUBPATH)) {
            JSONObject json = JSONParser.parseJSON(req);
            UserService.confirmUser(json.getInt("id"));
            return;
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
        if(oldUser == null){
            log.error("Could not update user with id: " + params.getInt("id") + ". User was not found in database");
        }

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
            log.error("Could not delete user");
            return;
        }
        int id = Integer.parseInt(pathInfo.substring(1));

        UserService.deleteUser(id);
    }
}
