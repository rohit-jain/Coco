package com.example.rohitjain.coco;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohitjain on 20/10/15.
 */
public class BoxBoundary extends Boundary {

    Integer categoryId;
    String label;

    BoxBoundary(JSONObject jsonBbox) {
        super(jsonBbox);
        try {
            JSONObject bboxes = jsonBbox.getJSONObject("bbox");
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
        if( touchX > x && touchX<(x + width)  && touchY > y && touchY<(y + height) ){
            this.touched = true;
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
}
