package com.example.rohitjain.coco;

import android.app.ActivityOptions;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, OnInitListener, View.OnClickListener, HandleResponse{

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    TextToSpeech tts;


    // Bounding boxes and category id
    final List<Boundary> boundaryList = new ArrayList<Boundary>();

    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.US);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.next){
//            getWindow().getDecorView().findViewById(R.id.imageView).invalidate();
//            initDownloadImageJson();
            Intent intent = new Intent(MainActivity.this, CaptionActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else if(v.getId() == R.id.prev){

        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        getWindow().getDecorView().findViewById(R.id.imageView).invalidate();
        initDownloadImageJson();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
//        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        int total = boundaryList.size();
        final TextView tv = (TextView) findViewById(R.id.textView);

        for(Boundary b:boundaryList){
            if(b.isTouched() == true){
                total -= 1;
            }
        }

        String speechObjectsLeft = String.valueOf(total) + " left";
        String textObjectsLeft = String.valueOf(total) + "objects left";
        tts.speak( speechObjectsLeft, TextToSpeech.QUEUE_FLUSH, null);
        tv.setText(textObjectsLeft);


        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    @Override
    protected void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        Log.d("Main", "Starting main");

        // set an exit transition
        getWindow().setExitTransition(new Explode());
        tts = new TextToSpeech(this, this);
        setContentView(R.layout.activity_main);

        FloatingActionButton nextButton = (FloatingActionButton)findViewById(R.id.next);
        FloatingActionButton prevButton = (FloatingActionButton)findViewById(R.id.prev);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        initDownloadImageJson();
    }

    public void initDownloadImageJson(){
        String RANDOM_IMAGE_URL = "http://"+ getString(R.string.CURRENT_IP) +":8000/experiment/random";
        Log.d("init download", RANDOM_IMAGE_URL);
        new DownloadImageJson(this).execute(RANDOM_IMAGE_URL);
    }

    @Override
    public void processFinish(String output) {
        String jsonString;
        String imageFileName = "";

        jsonString = output;
        Log.v("processingJson", output);

        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray bboxes = obj.getJSONArray("bboxes");
            JSONArray ocr = obj.getJSONArray("ocr");
            imageFileName = obj.getString("file_name");
            boundaryList.clear();
            for (int i = 0; i < bboxes.length(); i++)
            {
                boundaryList.add(new BoxBoundary(bboxes.getJSONObject(i)));
            }
            for (int i = 0; i < ocr.length(); i++){
                boundaryList.add(new AngleBoundary(bboxes.getJSONObject(i)));
            }

        } catch (JSONException e) {
            Log.v("Oncreate", "Error while decoding json");
            e.printStackTrace();
        }

        // this is the view on which you will listen for touch events
        String IMAGE_URL_STRING = "http://"+ getString(R.string.CURRENT_IP) +":8000/static/" + imageFileName;
        // downloads and sets the image
//            new DownloadImageTask((ImageView) findViewById(R.id.imageView), MainActivity.this).execute(IMAGE_URL_STRING);
        CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);

        new DownloadImageTask((ImageView) findViewById(R.id.imageView), progressView, true).execute(IMAGE_URL_STRING);

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final TextView tv = (TextView) findViewById(R.id.textView);

        iv.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv.setText("points : " + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));

                // TODO: Mark only one object touched at a time, right now overlapping objects get marked as touch

                for (Boundary b : boundaryList) {
//                    Log.d("polygon","Checking "+ b.getCategoryName() );
                    if (b.isInside(((double) event.getX()), (double) event.getY())) {
                        try {
                            tv.setText("Category : " + b.getLabel());
                            tts.speak(String.valueOf(b.getLabel()), TextToSpeech.QUEUE_FLUSH, null);
                        } catch (Exception e) {
                            Log.v("tts", "error");
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });

    }
}