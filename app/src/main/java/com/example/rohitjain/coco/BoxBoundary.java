package com.example.rohitjain.coco;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohitjain on 20/10/15.
 */
public class BoxBoundary extends Boundary {

    Integer categoryId;
    Double x;
    Double y;
    Double height;
    Double width;


    BoxBoundary(JSONObject jsonBbox) {
        super();
        try {
            JSONObject bboxes = jsonBbox.getJSONObject("bbox");
            this.x = bboxes.getDouble("x");
            this.y = bboxes.getDouble("y");
            this.width = bboxes.getDouble("w");
            this.height = bboxes.getDouble("h");
            this.categoryId = jsonBbox.getInt("category_id");
            this.label = jsonBbox.getString("category_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setVertices();
    }


    Integer getCategoryId(){
        return this.categoryId;
    }

    String getLabel(){
        return this.label;
    }

    Boolean isInside(Double touchX, Double touchY){
        if( touchX > this.x && touchX<(this.x + this.width)  && touchY > this.y && touchY<(this.y + this.height) ){
            return true;
        }
        return false;
    }

    void setVertices(){
        this.vertices.add(new Point(this.x, this.y));
        this.vertices.add(new Point(this.x + this.width, this.y));
        this.vertices.add(new Point(this.x + this.width, this.y + this.height));
        this.vertices.add(new Point(this.x, this.y + height));
    }

    @Override
    void scale(float scale_x, float scale_y){
        Log.v("Scaling", "scaling box");
        for(Point p: this.vertices){
            p.x = (p.x * scale_x);
            p.y = (p.y * scale_y);
        }
        this.x = this.x*scale_x;
        this.y = this.y*scale_y;
        this.width = (this.width*scale_x);
        this.height = (this.height*scale_y);
    }
}
