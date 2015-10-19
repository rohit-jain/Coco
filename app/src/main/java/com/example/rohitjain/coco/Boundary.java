package com.example.rohitjain.coco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    Integer getCategoryId(){
        return this.categoryId;
    }

    String getCategoryName(){
        return this.categoryName;
    }

    Boolean isInside(Double touchX, Double touchY){
        if( touchX > x && touchX<(x + width)  && touchY > y && touchY<(y + height) ){
            this.touched = true;
            return true;
        }
        return false;
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
