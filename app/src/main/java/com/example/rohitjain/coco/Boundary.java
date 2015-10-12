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

    Integer getCategoryId(){
        return this.categoryId;
    }

    Boolean isInside(Double touchX, Double touchY){
        if( ( touchX > x && touchX<(x + width) ) && ( touchY > y && touchY<(y + height) ) ){
            return true;
        }
        return false;
    }

    Boundary(Double x, Double y, Double height, Double width, Integer categoryId){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.categoryId = categoryId;
    }

    Boundary(JSONObject jsonBbox){

        try {
            JSONObject bboxes = jsonBbox.getJSONObject("bbox");
            this.x = bboxes.getDouble("x");
            this.y = bboxes.getDouble("y");
            this.width = bboxes.getDouble("w");
            this.height = bboxes.getDouble("h");
            this.categoryId = jsonBbox.getInt("category_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
