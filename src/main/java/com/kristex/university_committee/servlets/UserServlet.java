package com.kristex.university_committee.servlets;

import com.kristex.university_committee.dao.impl.UserDaoImpl;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.Faculty;
import com.kristex.university_committee.service.UserService;
import com.kristex.university_committee.service.FacultyService;
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
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");


        UserDaoImpl abiturientDao = new UserDaoImpl();
        PrintWriter out = resp.getWriter();

        String abiturientID = req.getPathInfo();

        if(abiturientID != null) {

//one abiturient by id

            abiturientID = abiturientID.substring(1);

            User user = UserService.getInstance().GetUserById(Integer.parseInt(abiturientID));
            if(user ==null){
                out.println("Abiturient not found");
                return;
            }

            //print

            JSONObject userJSON = JSONParser.getInstance().getUserJSON(user);

            Faculty faculty = FacultyService.getInstance().GetFacultyById(user.getFacultyId());
            JSONObject facJSON = JSONParser.getInstance().getFacultyJSON(faculty);

            userJSON.put("faculty", facJSON.getString("name"));

            out.println(userJSON);
        }
        else {

//all abiturients

            List<User> userList = UserService.getInstance().GetAllUser();
            userList.sort((o1, o2) -> o1.getId() > o2.getId() ? 1 : -1);

            JSONArray array = new JSONArray();

            for (User user : userList) {
                array.put(JSONParser.getInstance().getUserJSON(user));
            }
            out.println(array);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> paramMap = req.getParameterMap();
        System.out.println(paramMap.get("first_name")[0]);
    }
}
