package com.example.rohitjain.coco;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final TextView textView = (TextView) findViewById(R.id.textView);

        // Bounding boxes and category id
        final List<Boundary> boundaryList = new ArrayList<Boundary>();

        String jsonString = new String("{\"bboxes\": [{\"category_id\": 18, \"bbox\": {\"y\": 191.27, \"x\": 279.7, \"w\": 328.75, \"h\": 187.61}}, {\"category_id\": 1, \"bbox\": {\"y\": 30.39, \"x\": 0.26, \"w\": 134.17, \"h\": 192.36}}, {\"category_id\": 1, \"bbox\": {\"y\": 2.15, \"x\": 156.04, \"w\": 146.47, \"h\": 215.4}}, {\"category_id\": 1, \"bbox\": {\"y\": 13.4, \"x\": 81.37, \"w\": 164.66, \"h\": 215.4}}, {\"category_id\": 1, \"bbox\": {\"y\": 1.22, \"x\": 295.66, \"w\": 141.55, \"h\": 192.92}}, {\"category_id\": 41, \"bbox\": {\"y\": 307.29, \"x\": 231.67, \"w\": 271.87, \"h\": 72.76}}, {\"category_id\": 1, \"bbox\": {\"y\": 2.87, \"x\": 504.5, \"w\": 109.13, \"h\": 168.49}}, {\"category_id\": 1, \"bbox\": {\"y\": 50.9, \"x\": 214.66, \"w\": 134.99, \"h\": 152.69}}]}");
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray bboxes = obj.getJSONArray("bboxes");
            for (int i = 0; i < bboxes.length(); i++)
            {
                boundaryList.add(new Boundary(bboxes.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.v("Oncreate", "Error while decoding json");
            e.printStackTrace();
        }


        // this is the view on which you will listen for touch events
        String MY_URL_STRING = "http://farm8.staticflickr.com/7119/7485496446_fe4e9ef50c_z.jpg";
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(MY_URL_STRING);

        t1 =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });


        imageView.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.setText("points : " + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));

                for(Boundary b: boundaryList){
                    if(b.isInside(((double) event.getX()), (double)event.getY())) {
                        try {
                            textView.setText("Category : " + String.valueOf(b.getCategoryId()) + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
                            t1.speak("Category" + String.valueOf( b.getCategoryId() ), TextToSpeech.QUEUE_FLUSH, null);
                        }
                        catch (Exception e){
                            Log.v("tts","error");
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });
    }
}
