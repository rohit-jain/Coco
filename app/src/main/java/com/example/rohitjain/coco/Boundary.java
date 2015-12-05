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
    Boolean touched;
    String label;
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

    String getLabel(){
        return this.label;
    }

    Boolean getTouched(){
        return this.touched;
    }

    void setTouched(){
        this.touched = true;
    }

    Boundary(){

        this.label = "";
        this.touched = false;
    }


}