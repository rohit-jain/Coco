package com.example.rohitjain.coco;

import android.util.Log;

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

    void scale(float scale_x, float scale_y){
        Log.v("Scaling", "default scaling called - not implemented");
    }

    Boundary(){

        this.label = "";
        this.touched = false;
    }


}