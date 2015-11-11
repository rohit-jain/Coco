package com.example.rohitjain.coco;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Queue;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, OnInitListener, View.OnClickListener, HandleResponse{

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    String imageId = "";
    TextToSpeech tts;
    int GLOBAL_TOUCH_POSITION_X = 0;
    int GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
    int pointerCount;
    Boolean firstTime = Boolean.FALSE;

    HashMap<String, String> ttsMap = new HashMap<String, String>();
    public static List<Boundary> ttsList = new ArrayList<Boundary>();
    // Bounding boxes and category id
    final List<Boundary> boundaryList = new ArrayList<Boundary>();

    public static void removeFromTtsList(Boundary b){
        ttsList.remove(b);
    }

    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.US);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.captions_button){
            Intent intent = new Intent(MainActivity.this, CaptionActivity.class);
            Bundle b = new Bundle();
            b.putString("imageId", imageId); //Your id
            intent.putExtras(b);
//            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            startActivity(intent);
        }
        else if(v.getId() == R.id.next_image_button){
            getWindow().getDecorView().findViewById(R.id.imageView).invalidate();
            initDownloadImageJson();
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
//        pointerCount = event.getPointerCount();
//        Log.d(DEBUG_TAG, Integer.toString(pointerCount));

        this.mDetector.onTouchEvent(event);
        //Number of touches
//        if(pointerCount == 2){
//            int action = event.getActionMasked();
//            int actionIndex = event.getActionIndex();
//            switch (action)
//            {
//                case MotionEvent.ACTION_DOWN:
//                    GLOBAL_TOUCH_POSITION_X = (int) event.getX(1);
//                    break;
//                case MotionEvent.ACTION_UP:
//                    GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
//                    getWindow().getDecorView().findViewById(R.id.imageView).invalidate();
//                    initDownloadImageJson();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//
//                    GLOBAL_TOUCH_CURRENT_POSITION_X = (int) event.getX(1);
//                    int diff = GLOBAL_TOUCH_CURRENT_POSITION_X - GLOBAL_TOUCH_POSITION_X;
//                    Log.d("double drag",Integer.toString(diff));
//                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    GLOBAL_TOUCH_POSITION_X = (int) event.getX(1);
//                    break;
//                default:
//                    break;
//            }
//
//        }
//        else {
//            GLOBAL_TOUCH_POSITION_X = 0;
//            GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
//        }
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
//        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
//        Log.d(DEBUG_TAG, Integer.toString(pointerCount));
//        if(pointerCount == 2) {
//            getWindow().getDecorView().findViewById(R.id.imageView).invalidate();
//            initDownloadImageJson();
//        }
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

        HashMap<String, Integer> freq = new HashMap<String, Integer>();
        for(Boundary b:boundaryList){
            if(b.isTouched() == false){
                String word = b.getLabel();
                int count = freq.containsKey(word) ? freq.get(word) : 0;
                freq.put(word, count + 1);
                total -= 1;
            }
        }

        List<String> speechObjectsLeft = new ArrayList<String>();

        for(String k:freq.keySet()){
            if(freq.get(k)!=1) {
                speechObjectsLeft.add(freq.get(k) + " " + k );
            }else {
                speechObjectsLeft.add(k) ;
            }
        }

        String textObjectsLeft = "";

        if( speechObjectsLeft.size() == 0 )
            textObjectsLeft = "No objects left";
        else {
            for (int i = 0; i < speechObjectsLeft.size(); i++) {
                if (i != (speechObjectsLeft.size() - 1))
                    textObjectsLeft += ("\"" + speechObjectsLeft.get(i) +"\"" + ",");
                else
                    textObjectsLeft += ("\"" + speechObjectsLeft.get(i) +"\"");
            }
        }

        tts.speak( textObjectsLeft, TextToSpeech.QUEUE_FLUSH, null);
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
//        getWindow().setExitTransition(new Fade());
        tts = new TextToSpeech(this, this);
        setContentView(R.layout.activity_main);

//        FloatingActionButton nextButton = (FloatingActionButton)findViewById(R.id.next);
//        FloatingActionButton prevButton = (FloatingActionButton)findViewById(R.id.prev);
        Button nextImageButton = (Button) findViewById(R.id.next_image_button );
        Button showCaptionButton = (Button) findViewById(R.id.captions_button );

//        nextButton.setOnClickListener(this);
//        prevButton.setOnClickListener(this);
        nextImageButton.setOnClickListener(this);
        showCaptionButton.setOnClickListener(this);
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
            firstTime = Boolean.TRUE;
            // first time task
            final View overlay = findViewById(R.id.overlayOnBoarding);
            final View overlay2 = findViewById(R.id.overlayOnBoarding2);
            final View overlay3 = findViewById(R.id.overlayOnBoarding3);

            overlay.setVisibility(View.VISIBLE);

            Button overlayButton1 = (Button) findViewById(R.id.buttonOverlay1);
            Button overlayButton2 = (Button) findViewById(R.id.buttonOverlay2);
            Button overlayButton3 = (Button) findViewById(R.id.buttonOverlay3);

            overlayButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overlay.setVisibility(View.GONE);
                    overlay2.setVisibility(View.VISIBLE);
                }
            });

            overlayButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overlay2.setVisibility(View.GONE);
                    overlay3.setVisibility(View.VISIBLE);
                }
            });

            overlayButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overlay3.setVisibility(View.GONE);
                }
            });


            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

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
        String RANDOM_IMAGE_URL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/random";
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
            JSONArray segmentation = obj.getJSONArray("segmentation");
            JSONArray ocr = obj.getJSONArray("ocr");
            imageFileName = obj.getString("file_name");
            Boolean RLE = obj.getBoolean("RLE");
            imageId = obj.getString("image_id");

            boundaryList.clear();
            if(RLE == true) {
                for (int i = 0; i < bboxes.length(); i++)
                {
                    boundaryList.add(new BoxBoundary(bboxes.getJSONObject(i)));
                }
            }
            else {
                Log.d("List", "Polygons");
                for (int i = 0; i < segmentation.length(); i++) {
                    boundaryList.add(new PolygonBoundary(segmentation.getJSONObject(i)));
                }
            }

            for (int i = 0; i < ocr.length(); i++){
                boundaryList.add(new AngleBoundary(ocr.getJSONObject(i)));
            }

        } catch (JSONException e) {
            Log.v("Oncreate", "Error while decoding json");
            e.printStackTrace();
        }

        tts.setOnUtteranceProgressListener(new TextSpeechHandler(boundaryList));

        // this is the view on which you will listen for touch events
        String IMAGE_URL_STRING = "http://"+ getString(R.string.CURRENT_IP) +"/static/" + imageFileName;
        // downloads and sets the image
//            new DownloadImageTask((ImageView) findViewById(R.id.imageView), MainActivity.this).execute(IMAGE_URL_STRING);
        CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);

        if(firstTime == true) {
            new DownloadImageTask((ImageView) findViewById(R.id.imageView), (ImageView) findViewById(R.id.imageBlankView), progressView, false).execute(IMAGE_URL_STRING);
        }
        else{
            new DownloadImageTask((ImageView) findViewById(R.id.imageView), null, progressView, false).execute(IMAGE_URL_STRING);
        }
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final TextView tv = (TextView) findViewById(R.id.textView);

        iv.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                tv.setText("points : " + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));

                // TODO: Mark only one object touched at a time, right now overlapping objects get marked as touch

                for (Boundary b : boundaryList) {
//                    Log.d("polygon","Checking "+ b.getCategoryName() );
                    if (b.isInside(((double) event.getX()), (double) event.getY())) {
                        try {
                            tv.setText("Category : " + b.getLabel());
                            if(!ttsList.contains(b)) {
                                ttsMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, Integer.toString(boundaryList.indexOf(b)));
                                ttsList.add(b);
                                tts.speak(String.valueOf(b.getLabel()), TextToSpeech.QUEUE_ADD, ttsMap);
                            }
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