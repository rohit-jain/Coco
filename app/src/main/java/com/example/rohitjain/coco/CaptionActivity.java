package com.example.rohitjain.coco;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaptionActivity extends AppCompatActivity implements View.OnClickListener, HandleResponse{

    String imageId;
    CircularProgressView progressView;
    HashMap<Integer, String> captionMapping = new HashMap<Integer, String>();

    @Override
    public void processFinish(String output) {
        String jsonString;
        String imageFileName = "";
        ArrayList<TextView> tvs = new ArrayList<TextView>();

        tvs.add((TextView)findViewById(R.id.c1));
        tvs.add((TextView)findViewById(R.id.c2));
        tvs.add((TextView)findViewById(R.id.c3));

        jsonString = output;
        Log.v("processingJson", output);

        try {
            JSONArray jsonCaptions = new JSONArray(output);
            for(int i=0; i< jsonCaptions.length(); i++){
                String captionImageId = jsonCaptions.getJSONObject(i).getString("image_id");
                String captionText = jsonCaptions.getJSONObject(i).getString("caption");
                TextView tv = tvs.get(i);
                tv.setText(captionText);
                captionMapping.put(tv.getId(), captionImageId);
                tv.setOnClickListener(this);
            }

        } catch (JSONException e) {

        }

        progressView.setVisibility(View.INVISIBLE);
        findViewById(R.id.captions).setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);
        findViewById(R.id.captions).setVisibility(View.INVISIBLE);

        Bundle b = getIntent().getExtras();
        imageId = b.getString("imageId");

        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();

        String CAPTION_URL = "http://"+ getString(R.string.CURRENT_IP) +":8000/experiment/surveyq/" + imageId;
        Log.d("Task download caption", CAPTION_URL);
        new DownloadImageJson(this).execute( CAPTION_URL );
        // set an exit transition
        getWindow().setExitTransition(new Explode());

        FloatingActionButton nextButton = (FloatingActionButton)findViewById(R.id.next);
        FloatingActionButton prevButton = (FloatingActionButton)findViewById(R.id.prev);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.next){
            Intent intent = new Intent(CaptionActivity.this, ImageActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else if(v.getId() == R.id.prev){

        }
        else{
            Intent intent = new Intent(CaptionActivity.this, ImageActivity.class);
            Bundle b = new Bundle();
            b.putString("imageId", captionMapping.get(v.getId()));
            intent.putExtras(b);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }

    }
}
