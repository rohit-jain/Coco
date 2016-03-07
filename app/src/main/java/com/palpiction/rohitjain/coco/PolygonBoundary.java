package com.palpiction.rohitjain.coco;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohitjain on 29/10/15.
 */
public class PolygonBoundary extends Boundary{



    PolygonBoundary(JSONObject polygonJson){
        super();
        Log.d("polygon json", polygonJson.toString());
        try {
            this.label = polygonJson.getString("category_name");
            setVertices(polygonJson.getString("points"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Boolean isInside(Double touchX, Double touchY){
        boolean result = false;
        int nVertices = this.vertices.size();
        int i,j;
//        Log.d("polygon", touchX.toString() + "," + touchY.toString());
//        Log.d("polygon", this.x.toString() + "," + this.y.toString() + "," + this.width.toString() + "," + this.height.toString());
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

//                    Log.d("polygon", vxi.toString() +"," + vyi.toString() +"," + vxj.toString()  +"," + vyj.toString() );
//                    Log.d("polygon", condition_1.toString() + "," +condition_2.toString() +"," + condition_3.toString());
                }
            }
        }
        return result;
    }


    void setVertices(String pointsString){
        JSONArray points = null;
        try {
            points = new JSONArray(pointsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0; i< points.length(); i++){
            try {

                JSONArray coords = points.getJSONArray(i);
                this.vertices.add(new Point(coords.getDouble(0), coords.getDouble(1)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void scale(float scale_x, float scale_y){
        Log.v("Scaling", "scaling polygon");
        for(Point p: this.vertices){
            p.x = (p.x * scale_x);
            p.y = (p.y * scale_y);
        }
    }

}
