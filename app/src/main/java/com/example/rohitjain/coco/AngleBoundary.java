package com.example.rohitjain.coco;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohitjain on 20/10/15.
 */
public class AngleBoundary extends Boundary {
    Double theta;

    AngleBoundary(JSONObject jsonBbox) {
        super(jsonBbox);
        try {

            JSONObject bboxes = jsonBbox.getJSONObject("bbox");
            this.theta = bboxes.getDouble("a");
            this.label = bboxes.getString("string");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setVertices();
    }

    String getLabel(){
        return this.label;
    }

    void setVertices(){
        Double deltaX = (this.width * Math.cos(this.theta));
        Double deltaY = (this.width * Math.cos(this.theta));
        this.vertices.add(new Point( this.x, this.y ));
        this.vertices.add(new Point( (this.x + deltaX ) , (this.y + deltaY ) ) );
        this.vertices.add(new Point( (this.x + deltaX ),  (this.y + this.height - deltaY)));
        this.vertices.add(new Point( this.x, this.y + height));
    }

    Boolean isInside(Double touchX, Double touchY){
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



}
