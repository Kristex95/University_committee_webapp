package com.kristex.university_committee.servlets;

import com.kristex.university_committee.utils.JWTUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "authFilter")
public class AuthMiddleware implements Filter {
    private final static String AUTH_HEADER = "Authorization";
    private final static String AUTH_HEADER_START = "Bearer ";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String authorizationHeader = httpRequest.getHeader(AUTH_HEADER);

        if(authorizationHeader != null && authorizationHeader.startsWith(AUTH_HEADER_START)){
            String token = authorizationHeader.substring(AUTH_HEADER_START.length());
            JSONObject jsonObject = JWTUtils.getTokenParams(token);
            if (jsonObject != null){
                httpRequest.setAttribute("params", jsonObject);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else {
                System.out.println("401");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        else{
            httpRequest.setAttribute("params", null);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
