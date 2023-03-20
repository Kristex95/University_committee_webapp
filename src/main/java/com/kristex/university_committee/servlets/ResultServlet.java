package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.ResultDaoImpl;
import com.kristex.university_committee.model.Result;
import com.kristex.university_committee.service.ResultService;
import com.kristex.university_committee.utils.JSONParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/result/*")
public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        ResultDaoImpl resultDao = new ResultDaoImpl();
        PrintWriter out = resp.getWriter();

        String resultID = req.getPathInfo();

        if(resultID != null){
            resultID = resultID.substring(1);

            Result result = ResultService.getInstance().GetResultById(Integer.valueOf(resultID));
            if(result == null){
                out.println("Result not found");
                return;
            }

            out.println(result);
        }
        else {
            List<Result> resultList = resultDao.GetAllResults();
            JSONArray jsonArray = new JSONArray();
            for (Result result : resultList){
                jsonArray.put(JSONParser.getInstance().getResultJSON(result));
            }

            out.println(jsonArray);
        }
    }
}
