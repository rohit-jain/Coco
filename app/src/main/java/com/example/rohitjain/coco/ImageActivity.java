package com.example.rohitjain.coco;

import android.app.ActivityOptions;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener, HandleResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // set an exit transition
        getWindow().setExitTransition(new Explode());

        FloatingActionButton nextButton = (FloatingActionButton)findViewById(R.id.next);
        FloatingActionButton prevButton = (FloatingActionButton)findViewById(R.id.prev);
        Button yesButton = (Button) findViewById(R.id.yes);
        Button noButton = (Button) findViewById(R.id.no);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        initDownloadImageJson(getIntent().getExtras().getString("imageId"));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.next){
//            Intent intent = new Intent(CaptionActivity.this, ImageActivity.class);
//            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else if(v.getId() == R.id.prev){

        }
        else {
            finish();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }


    public void initDownloadImageJson(String imageId){
        String CHOSEN_IMAGE_URL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/surveyc/"+imageId;
        new DownloadImageJson(this).execute(CHOSEN_IMAGE_URL);
    }

    @Override
    public void processFinish(String output) {
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
}
