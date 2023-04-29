package com.kristex.university_committee.filter;

import com.kristex.university_committee.utils.JWTUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "authFilter")
public class AuthMiddleware implements Filter {
    private static final Logger log = Logger.getLogger(AuthMiddleware.class);
    private final static String AUTH_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer ";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String authorizationHeader = httpRequest.getHeader(AUTH_HEADER);

        if(authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)){
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            JSONObject jsonObject = JWTUtils.getTokenParams(token);
            if (jsonObject != null){
                httpRequest.setAttribute("params", jsonObject);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else {
                log.error("Could not get user params from token");
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        else{
            httpRequest.setAttribute("params", null);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
