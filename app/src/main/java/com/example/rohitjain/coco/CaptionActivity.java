package com.example.rohitjain.coco;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
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
    int captionsUsed, doubleTapUsed;
    CircularProgressView progressView;
    HashMap<Integer, String> captionMapping = new HashMap<Integer, String>();

    @Override
    public void downloadComplete(String output) {
        ArrayList<TextView> tvs = new ArrayList<TextView>();

        tvs.add((TextView)findViewById(R.id.c1));
        tvs.add((TextView)findViewById(R.id.c2));
        tvs.add((TextView)findViewById(R.id.c3));

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
        captionsUsed = b.getInt("captionsUsed");
        doubleTapUsed = b.getInt("doubleTapUsed");

        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();

        String CAPTION_URL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/surveyq/" + imageId;
        Log.d("Task download caption", CAPTION_URL);
        new DownloadImageJson(this, DownloadImageJson.TaskType.DOWNLOAD_IMAGE).execute( CAPTION_URL );

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CaptionActivity.this, ImageActivity.class);
        Bundle b = new Bundle();
        b.putString("captionImageId", captionMapping.get(v.getId()));
        b.putString("imageId", imageId); //Your id
        b.putInt("captionsUsed", captionsUsed);
        b.putInt("doubleTapUsed", doubleTapUsed);

        intent.putExtras(b);
        startActivity(intent);

    }

    @Override
    public void removeFromTtsList(Boundary b) {

    }

    @Override
    public void setUsername(String output) {

    }
}
