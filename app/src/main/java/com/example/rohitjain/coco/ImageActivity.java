package com.example.rohitjain.coco;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener, HandleResponse{

    final String SHARED_PREFERENCE_FILE = "COCO_PREFERENCES";
    final String USERNAME = "username"; // key for shared pref file

    String imageId, captionType, captionImageId;
    int captionsUsed, doubleTapUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Button yesButton = (Button) findViewById(R.id.yes);
        Button noButton = (Button) findViewById(R.id.no);

        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        imageId = b.getString("imageId");
        captionsUsed = b.getInt("captionsUsed");
        doubleTapUsed = b.getInt("doubleTapUsed");
        captionType = b.getString("captionType");
        captionImageId = b.getString("captionImageId");

        initDownloadImageJson(b.getString("captionImageId"));
    }

    @Override
    public void onClick(View v) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        final String DEFAULT_USERNAME = "daffi";
        final String URL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/recordoutcome";
        nameValuePairs.add(new BasicNameValuePair("username", settings.getString(USERNAME, DEFAULT_USERNAME)));
        nameValuePairs.add(new BasicNameValuePair("image_id", imageId));
        nameValuePairs.add(new BasicNameValuePair("double_used", Integer.toString(doubleTapUsed)));
        nameValuePairs.add(new BasicNameValuePair("captions_used", Integer.toString(captionsUsed)));
        nameValuePairs.add(new BasicNameValuePair("caption_type", captionType));
        nameValuePairs.add(new BasicNameValuePair("caption_image_id", captionImageId));


        if(v.getId() == R.id.yes){
            nameValuePairs.add(new BasicNameValuePair("image_response", Boolean.toString(true)));
        }
        else if(v.getId() == R.id.no){
            nameValuePairs.add(new BasicNameValuePair("image_response", Boolean.toString(false)));
        }

        new sendPost(nameValuePairs).execute(URL);
        finish();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        settings.edit().putBoolean(MainActivity.LOAD_NEW_IMAGE,true).commit();
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);

    }


    public void initDownloadImageJson(String imageId){
        String CHOSEN_IMAGE_URL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/surveyc/"+imageId;
        new DownloadImageJson(this, DownloadImageJson.TaskType.DOWNLOAD_IMAGE).execute(CHOSEN_IMAGE_URL);
    }

    @Override
    public void downloadComplete(String output, DownloadImageJson.TaskType task) {
        String jsonString;
        String imageFileName = "";

        jsonString = output;
        Log.v("processingJson", output);

        try {
            JSONObject obj = new JSONObject(jsonString);
            imageFileName = obj.getString("file_name");

        } catch (JSONException e) {
            Log.v("Oncreate", "Error while decoding json");
            e.printStackTrace();
        }

        // this is the view on which you will listen for touch events
        String IMAGE_URL_STRING = "http://"+ getString(R.string.CURRENT_IP) +"/static/" + imageFileName;
        // downloads and sets the image
        CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);
        new DownloadImageTask((ImageView) findViewById(R.id.imageView2), progressView, true).execute(IMAGE_URL_STRING);

    }


    @Override
    public void removeFromTtsList(Boundary b) {

    }

    @Override
    public void setUsername(String output) {

    }
}
