package com.example.rohitjain.coco;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohitjain on 20/10/15.
 */
public class AngleBoundary extends Boundary {
    Double theta;
    Double x;
    Double y;
    Double height;
    Double width;

    AngleBoundary(JSONObject jsonBbox) {
        super();
        try {

            JSONObject bboxes = jsonBbox.getJSONObject("bbox");
            Log.d("Angle BBox", bboxes.toString());
            this.x = bboxes.getDouble("x");
            this.y = bboxes.getDouble("y");
            this.width = bboxes.getDouble("w");
            this.height = bboxes.getDouble("h");
            this.theta = bboxes.getDouble("a");
            this.label = "Text \"" + jsonBbox.getString("string") +"\"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setVertices();
    }

    String getLabel(){
        return this.label;
    }

    Boolean isInside(Double touchX, Double touchY){
        boolean result = false;
        int nVertices = this.vertices.size();
        int i,j;

        for( i=0, j = nVertices -1; i< nVertices; j = i++){
            Double vyi = this.vertices.get(i).y;
            Double vxi = this.vertices.get(i).x;

            Double vyj = this.vertices.get(j).y;
            Double vxj = this.vertices.get(j).x;


            Boolean condition_1 = (vyi > touchY);
            Boolean condition_2 = (vyj > touchY);

            if(( condition_1 != condition_2 )){
                Double p = ( ((vxj - vxi) * (touchY - vyi)) / (vyj- vyi) ) + vxi;
                Boolean condition_3 = (touchX < p);

                if(condition_3) {
                    result = !result;
                }
            }
        }
        return result;
    }

    void setVertices(){
        Double deltaX = (this.width * Math.cos(this.theta));
        Double deltaY = (this.height * Math.cos(this.theta));
        this.vertices.add(new Point( this.x, this.y ));
        this.vertices.add(new Point( (this.x + deltaX ) , (this.y + deltaY ) ) );
        this.vertices.add(new Point( (this.x + deltaX ),  (this.y + this.height - deltaY)));
        this.vertices.add(new Point( this.x, this.y + this.height));
    }

    @Override
    void scale(float scale_x, float scale_y){
        Log.v("Scaling", "scaling ocr - not proper - check theta meaning");
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