package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.ResultDaoImpl;
import com.kristex.university_committee.model.Result;
import com.kristex.university_committee.model.Subject;
import com.kristex.university_committee.service.ResultService;
import com.kristex.university_committee.service.SubjectService;
import com.kristex.university_committee.utils.JSONParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

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

            Result result = ResultService.getInstance().getResultById(Integer.valueOf(resultID));
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();
        JSONObject json = JSONParser.parseJSON(req);

        if(pathInfo == null || pathInfo.equals("/")) { //create
            Result newResult = new Result(json.getInt("abit_id"), json.getInt("subj_id"), json.getFloat("grade"));
            ResultService.createResult(newResult);
        }
        else {
            Integer id = Integer.valueOf(pathInfo.substring(1));
            Result updatedResult = new Result(id, json.getInt("abit_id"), json.getInt("subj_id"), json.getFloat("grade"));

            ResultService.updateResult(updatedResult);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();

        if(pathInfo==null || pathInfo.equals("/")){
            return;
        }

        Integer id = Integer.valueOf(pathInfo.substring(1));

        ResultService.deleteResult(id);
    }
}
