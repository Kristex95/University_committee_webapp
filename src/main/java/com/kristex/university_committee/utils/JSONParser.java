package com.kristex.university_committee.utils;

import com.kristex.university_committee.model.Registration;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.Faculty;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

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
    public static JSONObject getUserJSON(User user){
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("first_name", user.getFirstName());
        json.put("last_name", user.getLastName());
        json.put("email", user.getEmail());
        json.put("role", user.getRole());
        json.put("cache", user.getCache());

        return json;
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
        json.put("school_mark", registration.getSchoolMark());
        json.put("math_mark", registration.getMathMark());
        json.put("history_mark", registration.getHistoryMark());
        json.put("english_mark", registration.getEnglishMark());

        return json;
    }

//endregion
}
