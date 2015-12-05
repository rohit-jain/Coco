package com.example.rohitjain.coco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, OnInitListener, View.OnClickListener, HandleResponse{

    private static final String GESTURE_DEBUG_TAG = "Main Activity Gestures", DOWNLOAD_TAG = "Download", TTS_TAG = "TTS", ACTIVITY_TAG = "MAIN ACTIVITY";
    private GestureDetectorCompat mDetector;
    String imageId = "";
    TextToSpeech tts;
    TextView tv;
    HashMap<String, String> ttsMap;
    List<Boundary> ttsList;
    List<Boundary> boundaryList;
    int captionsUsed = 0, doubleTapUsed = 0;

    Boolean firstTime = Boolean.FALSE;
    public static final String SHARED_PREFERENCE_FILE = "COCO_PREFERENCES";

    public static final String SHOWN_OVERLAY = "Shown Overlay"; // key for shared pref file
    public static final String USERNAME = "username"; // key for shared pref file
    public static final String SPEECH_RATE = "speech_rate";

    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.US);
            tts.setSpeechRate(getSpeechRate());
        }
    }

    public Float getSpeechRate(){
        final Float DEFAULT_SPEECH_RATE = new Float(3.0);
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        Log.v(TTS_TAG, "speech rate "+settings.getFloat(SPEECH_RATE, DEFAULT_SPEECH_RATE));

        return settings.getFloat(SPEECH_RATE, DEFAULT_SPEECH_RATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(ACTIVITY_TAG, "Creating main activity");

        if(tts == null){
            tts = new TextToSpeech(this, this);
        }

        boundaryList = new ArrayList<Boundary>();
        // Download json for the image to be shown
        initDownloadImageJson();

        // initialize shared preferences settings
        final String DEFAULT_USERNAME = "daffi";
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE);

        // Instantiate the gesture detector with the application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);

        // Set the gesture detector as the double tap listener.
        mDetector.setOnDoubleTapListener(this);


        // Instantiate buttons and set click listeners
        Button nextImageButton = (Button) findViewById(R.id.next_image_button );
        Button showCaptionButton = (Button) findViewById(R.id.captions_button );
        // text view to display objects tapped & left
        tv = (TextView) findViewById(R.id.textObject);

        nextImageButton.setOnClickListener(this);
        showCaptionButton.setOnClickListener(this);

        // check if the user got a username from server
        if(settings.getString(USERNAME, DEFAULT_USERNAME) == DEFAULT_USERNAME){
            String getUsernameUrl = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/getnextuser";
            new DownloadImageJson(this, DownloadImageJson.TaskType.GET_USERNAME).execute(getUsernameUrl);
        }

        // show overlays if the application has been started for the first time
        if (settings.getBoolean(SHOWN_OVERLAY, true)) {
            // if the app is being launched for first time, do the overlay
            Log.v("Main Activity", "First time, preparing overlay");
            firstTime = Boolean.TRUE;
            showTutorial();

            // record the fact that the app has been started at least once and overlay shown
            settings.edit().putBoolean(SHOWN_OVERLAY, false).commit();
        }

    }

    private void showTutorial(){
        final View overlayImage = findViewById(R.id.overlayOnBoarding);
        final View overlayDoubleTap = findViewById(R.id.overlayOnBoarding2);
        final View overlayShowCaptions = findViewById(R.id.overlayOnBoarding3);

        overlayImage.setVisibility(View.VISIBLE);

        Button overlayImageButtton = (Button) findViewById(R.id.buttonOverlay1);
        Button overlayDoubleTapButton = (Button) findViewById(R.id.buttonOverlay2);
        Button overlayShowCaptionsButton = (Button) findViewById(R.id.buttonOverlay3);

        overlayImageButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayImage.setVisibility(View.GONE);
                overlayDoubleTap.setVisibility(View.VISIBLE);
            }
        });

        overlayDoubleTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayDoubleTap.setVisibility(View.GONE);
                overlayShowCaptions.setVisibility(View.VISIBLE);
            }
        });

        overlayShowCaptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayShowCaptions.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
        if(tts != null) {
            tts.setSpeechRate(getSpeechRate());
        }
        Log.v(ACTIVITY_TAG, "Starting Main Activity");

    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Log.v(ACTIVITY_TAG, "Re-starting Main Activity");

        if(tts == null){
            tts = new TextToSpeech(this, this);
        }

        tts.setSpeechRate(getSpeechRate());

        // Download json for the image to be shown
        initDownloadImageJson();

        tv.setText("");
    }

    /*
    Download json for a random image from the server
     */
    void initDownloadImageJson(){
        String RANDOM_IMAGE_URL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/random";
        Log.v(DOWNLOAD_TAG, "start download image json");
        // Async task to download image json data from the url
        // initialised with the activity as callback
        new DownloadImageJson(this, DownloadImageJson.TaskType.DOWNLOAD_IMAGE).execute(RANDOM_IMAGE_URL);
    }

    @Override
    /*
     Captures click event on the activity
     Handles cases for button clicks here
     */
    public void onClick(View v) {
        // click came from 'show captions' button
        if(v.getId() == R.id.captions_button){

            Intent intent = new Intent(MainActivity.this, CaptionActivity.class);
            Bundle b = new Bundle();
            b.putString("imageId", imageId); //Your id
            b.putInt("captionsUsed", objectsTouched());
            b.putInt("doubleTapUsed", doubleTapUsed);
            // pass the image id to captions class
            intent.putExtras(b);
            startActivity(intent);
        }
        // click to get a different image
        else if(v.getId() == R.id.next_image_button){
            // remove the old image
            getWindow().getDecorView().findViewById(R.id.imageView).invalidate();
            // download new image json
            initDownloadImageJson();
        }
    }


    @Override
    /*
    Double tap gesture
    Show objects left
     */
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(GESTURE_DEBUG_TAG, "onDoubleTap: " + event.toString());

        String textObjectsLeft = "";
        HashMap<String, Integer> freq = new HashMap<String, Integer>();
        doubleTapUsed++;

        // Count frequency of each type of object that is not touched
        for(Boundary b:boundaryList){
            if(b.isTouched() == false){
                String word = b.getLabel();
                int count = freq.containsKey(word) ? freq.get(word) : 0;
                freq.put(word, count + 1);
            }
        }


        if( freq.size() == 0 )
            textObjectsLeft = "No objects left";
        else {
            List<String> objectsLeftList = new ArrayList<String>();
            for( String objectName: freq.keySet()){
                String objectLeftString = freq.get(objectName) + " " + objectName;
                objectsLeftList.add(objectLeftString);
            }
            textObjectsLeft = TextUtils.join(",", objectsLeftList);
        }

        // TODO: write different speak methods for old and new android version
        tts.speak( textObjectsLeft, TextToSpeech.QUEUE_FLUSH, null);

        // update text view to show objects left
        tv.setText(textObjectsLeft);

        return true;
    }

    @Override
    /*
    Callback method when download async task is complete
     */
    public void downloadComplete(String jsonString) {

        Log.v(DOWNLOAD_TAG, "processing JSON string:" + jsonString);

        setBoundaries(jsonString);

        // instantiate text to speech object
        tts.setOnUtteranceProgressListener(new TextSpeechHandler(boundaryList, this));
        ttsList = new ArrayList<Boundary>();
        ttsMap = new HashMap<String, String>();


        final ImageView iv = (ImageView) findViewById(R.id.imageView);

        // Handle touch event on image
        iv.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN && action!=MotionEvent.ACTION_CANCEL){

                    // TODO: Mark only one object touched at a time?, right now overlapping objects get marked as touch

                    List<Boundary> objectsTouched = new ArrayList<Boundary>();
                    Boundary objectTouched = null;
                    Log.v("Touch","touched "+(double) event.getX() +"," + (double) event.getY());

                    for (Boundary b : boundaryList) {
                        if (b.isInside(((double) event.getX()), (double) event.getY())) {
                            objectsTouched.add(b);
                        }
                    }

                    if(objectsTouched.size()>=1){
                        int index = new Random().nextInt(objectsTouched.size());
                        objectTouched = objectsTouched.get(index);
                        try {
                            String categoryLabel = objectTouched.getLabel();
                            String categoryId = Integer.toString(boundaryList.indexOf(objectTouched));

                            // display category of object that was touched
                            tv.setText("Category : " + categoryLabel);

                            if (!ttsList.contains(objectTouched)) {
                                // add to tts queue with the category id as utterance id for this request
                                ttsMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, categoryId);
                                tts.speak(categoryLabel, TextToSpeech.QUEUE_ADD, ttsMap);

                                // add it to the list of objects in speech queue
                                ttsList.add(objectTouched);
                            }
                        } catch (Exception e) {
                            Log.e(TTS_TAG, "error while adding objects to tts");
                            e.printStackTrace();
                        }

                    }

                }

                return true;
            }
        });

    }

    /*
    Handle clicks for menu items selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.tutorial:
                showTutorial();
                return true;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     Returns the number of objects that have been touched
     */
    private int objectsTouched(){

        int objectsTouched = 0;

        // Count frequency of each type of object that is not touched
        for(Boundary b:boundaryList){
            if(b.isTouched() == true){
                objectsTouched++;
            }
        }

        return objectsTouched;
    }

    /*
     Set object boundaries and initiate the image download
     */
    private void setBoundaries(String jsonString){

        String imageFileName = "";
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
                Log.v(DOWNLOAD_TAG, "Adding bounding boxes for RLE");
                for (int i = 0; i < bboxes.length(); i++)
                {
                    boundaryList.add(new BoxBoundary(bboxes.getJSONObject(i)));
                }
            }
            else {
                Log.v(DOWNLOAD_TAG, "Adding Polygon boundaries");
                for (int i = 0; i < segmentation.length(); i++) {
                    boundaryList.add(new PolygonBoundary(segmentation.getJSONObject(i)));
                }
            }

            // Angle boundary for OCR boundaries
            for (int i = 0; i < ocr.length(); i++){
                boundaryList.add(new AngleBoundary(ocr.getJSONObject(i)));
            }

        } catch (JSONException e) {
            Log.e(DOWNLOAD_TAG, "Error while decoding json: " + jsonString);
            e.printStackTrace();
        }

        CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);

        String IMAGE_URL_STRING = "http://"+ getString(R.string.CURRENT_IP) +"/static/" + imageFileName;

        // Fire asynctask to download image and set the grey box according to image dimension
        // For first time, set grey box on the overlay as well
        if(firstTime == true) {
            new DownloadImageTask((ImageView) findViewById(R.id.imageView), (ImageView) findViewById(R.id.imageBlankView), progressView, false).execute(IMAGE_URL_STRING);
        }
        else{
            new DownloadImageTask((ImageView) findViewById(R.id.imageView), null, progressView, true).execute(IMAGE_URL_STRING);
        }
    }

    /*
    callback when a tts utterance done
    removes the corresponding boundary from ttslist
     */
    public void removeFromTtsList(Boundary b){
        ttsList.remove(b);
    }

    /*
    Set username in shared preference file
     */
    @Override
    public void setUsername(String output) {
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE);

        settings.edit().putString(USERNAME,output).commit();

    }

    @Override
    protected void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            Log.v("Main", "destroying tts");
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

}