package com.kristex.university_committee.servlets;

import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.UserToken;
import com.kristex.university_committee.service.UserService;
import com.kristex.university_committee.utils.JSONParser;
import com.kristex.university_committee.utils.JWTUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        JSONObject reqJSON = JSONParser.parseJSON(req);

        if(pathInfo.equals("/login")){

            UserToken userToken = UserService.login(reqJSON.getString("email"), reqJSON.getString("pass"));
            Cookie cookie = new Cookie("access_token", userToken.getAccess());
            resp.addCookie(cookie);

        }
        else if(pathInfo.equals("/register")) {


            User user = new User(
                    reqJSON.getString("first_name"),
                    reqJSON.getString("last_name"),
                    reqJSON.getString("email"),
                    Role.valueOf(reqJSON.getString("role")),
                    UserService.hashPassword(reqJSON.getString("pass")));

            UserService.createUser(user);
        }
    }
}

