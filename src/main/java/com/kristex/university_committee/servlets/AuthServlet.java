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
import java.io.PrintWriter;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        JSONObject reqJSON = JSONParser.parseJSON(req);
        PrintWriter out = resp.getWriter();
        if(pathInfo.equals("/login")){

            User user = UserService.getByEmail(reqJSON.getString("email"));
            if(user == null){
                System.out.println("user not found");
                return;
            }
            if(user.getCache().equals(UserService.hashPassword(reqJSON.getString("pass")))){
                try {
                    String refreshToken = JWTUtils.createRefreshToken(user.getId(), user.getRole());
                    String accessToken = JWTUtils.createAccessToken(user.getId(), user.getRole());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("refresh_token", refreshToken);
                    jsonObject.put("access_token", accessToken);
                    out.println(jsonObject);
                    System.out.println("tokens created");
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            else {
                System.out.println("wrong password");
            }
        }
        else if(pathInfo.equals("/register")) {
            User checkUser = UserService.getByEmail(reqJSON.getString("email"));
            if(checkUser != null){
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User with this email already exists");
                return;
            }

            User user = new User(
                    reqJSON.getString("first_name"),
                    reqJSON.getString("last_name"),
                    reqJSON.getString("email"),
                    UserService.hashPassword(reqJSON.getString("pass")));

            UserService.createUser(user);
        }
    }
}

