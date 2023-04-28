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
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(AuthServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        JSONObject reqJSON = JSONParser.parseJSON(req);
        PrintWriter out = resp.getWriter();
        if(pathInfo.equals("/login")){

            User user = UserService.getByEmail(reqJSON.getString("email"), "local");
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
                    log.info("Local login successful");
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            else {
                System.out.println("wrong password");
                log.info("Wrong password");
            }
        }
        else if(pathInfo.equals("/login_auth0")){
            String token = reqJSON.getString("token");
            if(token == null){
                out.println(HttpServletResponse.SC_BAD_REQUEST);
            }
            else {
                JSONObject params = JWTUtils.getAuth0TokenPrams(token);
                User user = UserService.getByEmail(params.getString("email"), "auth0");
                if(user == null){
                    log.error("could not find user with email " + params.getString("email"));
                }
                JSONObject jsonPrams = JWTUtils.getAuth0TokenPrams(token);
                if(jsonPrams!=null) {
                    String refreshToken = JWTUtils.createRefreshToken(user.getId(), user.getRole());
                    String accessToken = JWTUtils.createAccessToken(user.getId(), user.getRole());
                    JSONObject tokensJSON = new JSONObject();
                    tokensJSON.put("refresh_token", refreshToken);
                    tokensJSON.put("access_token", accessToken);

                    out.println(tokensJSON);
                }
                else {
                    System.out.println("Auth0 Error");
                    log.error("auth0 error");
                    out.println(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
        else if(pathInfo.equals("/register")) {
            User checkUser = UserService.getByEmail(reqJSON.getString("email"), "local");
            if(checkUser != null){
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User with this email already exists");
                return;
            }

            User user = new User(
                    reqJSON.getString("first_name"),
                    reqJSON.getString("last_name"),
                    reqJSON.getString("email"),
                    UserService.hashPassword(reqJSON.getString("pass")));
            user.setType_auth("local");

            UserService.createUser(user);
        }else if(pathInfo.equals("/register_auth0")) {
            User user = new User(
                    reqJSON.getString("first_name"),
                    reqJSON.getString("last_name"),
                    reqJSON.getString("email"));
            user.setType_auth("auth0");
            System.out.println(user);

            UserService.createUser(user);
        }
    }
}

