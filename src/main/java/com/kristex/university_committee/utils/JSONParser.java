package com.kristex.university_committee.utils;

import com.kristex.university_committee.model.Registration;
import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.Faculty;
import com.kristex.university_committee.service.FacultyService;
import com.kristex.university_committee.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class JSONParser {

    private static JSONParser instance;

    public static synchronized JSONParser getInstance(){
        if(instance == null){
            instance = new JSONParser();
        }
        return instance;
    }

    public static JSONObject parseJSON(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while((line = reader.readLine()) != null){
            sb.append(line);
        }
        return new JSONObject(sb.toString());
    }

//region user
    public static JSONObject getUserWithFacultyJSON(User user){
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("first_name", user.getFirstName());
        json.put("last_name", user.getLastName());
        json.put("email", user.getEmail());
        json.put("role", user.getRole());
        json.put("school_mark", user.getSchool_mark());
        json.put("math_mark", user.getMath_mark());
        json.put("english_mark", user.getEnglish_mark());
        json.put("history_mark", user.getHistory_mark());
        json.put("confirmed", user.isConfirmed());

        List<Faculty> facultyList = FacultyService.GetAllFacultiesByUserId(user.getId());

        JSONArray array = new JSONArray();
        for (Faculty faculty: facultyList) {
            JSONObject facultyObject = new JSONObject();
            facultyObject.put("faculty", faculty.getName());
            facultyObject.put("id", faculty.getId());
            array.put(facultyObject);
        }
        json.put("faculties", array);

        return json;
    }

    public static JSONObject getUserSimpleJSON(User user){
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("first_name", user.getFirstName());
        json.put("last_name", user.getLastName());
        json.put("email", user.getEmail());
        json.put("role", user.getRole());

        return json;
    }
    public static JSONObject getUserJSON(User user){
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("first_name", user.getFirstName());
        json.put("last_name", user.getLastName());
        json.put("email", user.getEmail());
        json.put("role", user.getRole());
        json.put("school_mark", user.getSchool_mark());
        json.put("math_mark", user.getMath_mark());
        json.put("english_mark", user.getEnglish_mark());
        json.put("history_mark", user.getHistory_mark());
        json.put("confirmed", user.isConfirmed());

        return json;
    }

    public static User parseUserJSON(JSONObject json){
        User user = new User(
                json.getString("first_name"),
                json.getString("last_name"),
                json.getString("email"),
                Role.valueOf(json.getString("role")),
                json.getFloat("school_mark"),
                json.getInt("math_mark"),
                json.getInt("english_mark"),
                json.getInt("history_mark")
        );
        return user;
    }

//endregion

//region faculty
    public static JSONObject getFacultyJSON(Faculty faculty){
        JSONObject json = new JSONObject();
        json.put("id", faculty.getId());
        json.put("name", faculty.getName());

        return json;
    }
//endregion

//region registration

    public static JSONObject getRegistrationJSON(Registration registration){
        JSONObject json = new JSONObject();
        json.put("id", registration.getId());
        json.put("faculty_id", registration.getFacultyId());

        return json;
    }

//endregion

}
