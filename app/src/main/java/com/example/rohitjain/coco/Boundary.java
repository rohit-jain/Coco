package com.example.rohitjain.coco;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rohitjain on 11/10/15.
 */
public class Boundary {
    Double x;
    Double y;
    Double height;
    Double width;
    Integer categoryId;
    String categoryName;
    Boolean touched;
    ArrayList<Point> vertices = new ArrayList<Point>();

    class Point{
        double x;
        double y;

        Point(double x, double y){
            this.x = x;
            this.y = y;
        }

    }

    Boolean isInside(Double touchX, Double touchY){
        Log.d("Boundary","Checking inside the Boundary class");
        return false;
    }

    Integer getCategoryId(){
        return this.categoryId;
    }

    String getCategoryName(){
        return this.categoryName;
    }

    Boolean isTouched(){
        return this.touched;
    }

    Boundary(Double x, Double y, Double height, Double width, Integer categoryId, String categoryName){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.touched = false;
    }

    Boundary(JSONObject jsonBbox){

        try {
            JSONObject bboxes = jsonBbox.getJSONObject("bbox");
            this.x = bboxes.getDouble("x");
            this.y = bboxes.getDouble("y");
            this.width = bboxes.getDouble("w");
            this.height = bboxes.getDouble("h");
            this.categoryId = jsonBbox.getInt("category_id");
            this.categoryName = jsonBbox.getString("category_name");
            this.touched = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}