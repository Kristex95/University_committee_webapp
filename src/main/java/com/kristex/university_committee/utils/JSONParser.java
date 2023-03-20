package com.kristex.university_committee.utils;

import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.Faculty;
import com.kristex.university_committee.model.Result;
import com.kristex.university_committee.model.Subject;
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
        json.put("school_grade", user.getSchoolGrade());
        json.put("email", user.getEmail());
        json.put("role", user.getRole());
        json.put("cache", user.getCache());
        json.put("salt", user.getSalt());

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

//region subject
    public static JSONObject getSubjectJSON(Subject subject){
        JSONObject json = new JSONObject();
        json.put("id", subject.getId());
        json.put("name", subject.getName());

        return json;
    }
//endregion

//region result

    public static JSONObject getResultJSON(Result result){
        JSONObject json = new JSONObject();

        json.put("id", result.getId());
        json.put("abit_id", result.getAbitId());
        json.put("subj_id", result.getSubjId());
        json.put("grade", result.getGrade());

        return json;
    }

//endregion
}
