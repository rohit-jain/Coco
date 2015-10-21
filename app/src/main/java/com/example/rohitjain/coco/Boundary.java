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

    Boolean isInsidePolygon(Double touchX, Double touchY){
        boolean result = false;
        int nVertices = vertices.size();
        int i,j;
//        Log.d("polygon", touchX.toString() + "," + touchY.toString());
//        Log.d("polygon", this.x.toString() + "," + this.y.toString() + "," + this.width.toString() + "," + this.height.toString());
        for( i=0, j = nVertices -1; i< nVertices; j = i++){
            Double vyi = vertices.get(i).y;
            Double vxi = vertices.get(i).x;

            Double vyj = vertices.get(j).y;
            Double vxj = vertices.get(j).x;


            Boolean condition_1 = (vyi > touchY);
            Boolean condition_2 = (vyj > touchY);

            if(( condition_1 != condition_2 )){
                Double p = ( ((vxj - vxi) * (touchY - vyi)) / (vyj- vyi) ) + vxi;
                Boolean condition_3 = (touchX < p);

                if(condition_3) {
                    result = !result;

//                    Log.d("polygon", vxi.toString() +"," + vyi.toString() +"," + vxj.toString()  +"," + vyj.toString() );
//                    Log.d("polygon", condition_1.toString() + "," +condition_2.toString() +"," + condition_3.toString());
                }
            }
        }
        if(result && (!this.touched)){
                this.touched = true;
        }
        return result;
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
            this.vertices.add(new Point(this.x, this.y));
            this.vertices.add(new Point(this.x + this.width, this.y));
            this.vertices.add(new Point(this.x + this.width, this.y + this.height));
            this.vertices.add(new Point(this.x, this.y + height));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
