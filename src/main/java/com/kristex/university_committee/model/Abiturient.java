package com.kristex.university_committee.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Abiturient {
    int id;
    String first_name;
    String last_name;
    float school_grade;
    String faculty;
    Map<String, Integer> certificate_grades;

    public Abiturient(String first_name, String last_name, Float school_grade){
        this.first_name = first_name;
        this.last_name = last_name;
        this.school_grade = school_grade;
    }

    public String GetJSON(){
        JSONObject json = new JSONObject();
        json.put("first_name", first_name);
        json.put("last_name", last_name);
        json.put("school_grade", school_grade);
        json.put("faculty", faculty);

        JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        for(Map.Entry<String, Integer> grade : certificate_grades.entrySet()){
            item.put(grade.getKey(), grade.getValue());
        }
        array.put(item);
        json.put("certificate_grades", item);


        return json.toString();
    }
}
