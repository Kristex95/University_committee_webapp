package com.kristex.university_committee.servlets;

import com.google.gson.annotations.JsonAdapter;
import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.utils.JWTUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/refresh-token")
public class RefreshTokenServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(RefreshTokenServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonParams = (JSONObject) req.getAttribute("params");

        if(jsonParams != null){
            String newAccessToken = JWTUtils.createAccessToken(jsonParams.getInt("id"), Role.valueOf(jsonParams.getString("role")));
            String newRefreshToken = JWTUtils.createRefreshToken(jsonParams.getInt("id"), Role.valueOf(jsonParams.getString("role")));

            JSONObject tokens = new JSONObject();
            tokens.put("access_token", newAccessToken);
            tokens.put("refresh_token", newRefreshToken);
            resp.getWriter().write(tokens.toString());
        }
        else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("Tokens were not refreshed. 'jsonParams' is empty");
        }
    }
}
