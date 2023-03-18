package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.ResultDao;
import com.kristex.university_committee.dao.impl.ResultDaoImpl;
import com.kristex.university_committee.model.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/result/*")
public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        ResultDaoImpl resultDao = new ResultDaoImpl();
        PrintWriter out = resp.getWriter();

        String resultID = req.getPathInfo();

        if(resultID != null){
            resultID = resultID.substring(1);

            Result result = resultDao.GetResultById(Integer.valueOf(resultID));
            if(result == null){
                out.println("Result not found");
                return;
            }

            out.println("<p>" + result + "</p>");
        }
        else {
            List<Result> resultList = resultDao.GetAllResults();
            for (Result result : resultList){
                out.println("<p>" + result + "</p>");
            }
        }
    }
}
