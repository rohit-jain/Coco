package com.example.rohitjain.coco;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    String CURRENT_IP = "192.168.1.154";
    TextToSpeech t1;


    public class DownloadImageJson extends AsyncTask<String, Void, String> {
        private String readStream(InputStream is) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while(i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        protected String doInBackground(String... uri) {
            String responseString = new String();
            try {
                HttpURLConnection urlConnection;
                URL url = new URL(uri[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseString = readStream(in);
                }
                catch (Exception e) {
                    //TODO Handle problems..
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch(Exception e){
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String output) {

            String jsonString;
            String imageId, imageFileName = "";

            // Bounding boxes and category id
            final List<Boundary> boundaryList = new ArrayList<Boundary>();

            jsonString = output;
            Log.v("prcessingJson", output);

            try {
                JSONObject obj = new JSONObject(jsonString);
                JSONArray bboxes = obj.getJSONArray("bboxes");
                imageFileName = obj.getString("file_name");
                boundaryList.clear();
                for (int i = 0; i < bboxes.length(); i++)
                {
                    boundaryList.add(new Boundary(bboxes.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.v("Oncreate", "Error while decoding json");
                e.printStackTrace();
            }

            // this is the view on which you will listen for touch events
            String IMAGE_URL_STRING = "http://"+ CURRENT_IP +":8000/static/" + imageFileName;
            new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(IMAGE_URL_STRING);

            t1 =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.US);
                    }
                }
            });

            final ImageView iv = (ImageView) findViewById(R.id.imageView);
            final TextView tv = (TextView) findViewById(R.id.textView);

            iv.setOnTouchListener(new ImageView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    tv.setText("points : " + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));

                    for (Boundary b : boundaryList) {
                        if (b.isInside(((double) event.getX()), (double) event.getY())) {
                            try {
                                tv.setText("Category : " + b.getCategoryName() + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
                                t1.speak(String.valueOf(b.getCategoryName()), TextToSpeech.QUEUE_FLUSH, null);
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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        getWindow().getDecorView().findViewById(R.id.imageView).invalidate();
        String RANDOM_IMAGE_URL = "http://"+ CURRENT_IP +":8000/experiment/random";
        new DownloadImageJson().execute(RANDOM_IMAGE_URL);

        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        String RANDOM_IMAGE_URL = "http://"+ CURRENT_IP +":8000/experiment/random";
        new DownloadImageJson().execute(RANDOM_IMAGE_URL);

    }
}