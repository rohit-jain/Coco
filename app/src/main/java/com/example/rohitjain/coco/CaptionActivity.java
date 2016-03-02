package com.example.rohitjain.coco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CaptionActivity extends AppCompatActivity implements View.OnClickListener, HandleResponse{

    String imageId;
    int captionsUsed, doubleTapUsed;
    CircularProgressView progressView;
    HashMap<Integer, List<String>> captionMapping = new HashMap<Integer, List<String>>();

    @Override
    public void downloadComplete(String output, DownloadImageJson.TaskType task) {
        ArrayList<TextView> tvs = new ArrayList<TextView>();

        tvs.add((TextView)findViewById(R.id.c1));
        tvs.add((TextView)findViewById(R.id.c2));
        tvs.add((TextView)findViewById(R.id.c3));

        Log.v("processingJson", output);

        try {
            JSONArray jsonCaptionTuples = new JSONArray(output);
            for(int i=0; i< jsonCaptionTuples.length(); i++){
                JSONArray jsonCaptions = new JSONArray(jsonCaptionTuples.get(i).toString());

                JSONObject jsonCaption = jsonCaptions.getJSONObject(0);
                String captionImageId = jsonCaption.getString("image_id");
                String captionText = jsonCaption.getString("caption");
                String captionType = jsonCaptions.get(1).toString();

                TextView tv = tvs.get(i);
                tv.setText(captionText);
                captionMapping.put(tv.getId(), Arrays.asList(captionImageId, captionType));
                tv.setOnClickListener(this);
            }

        } catch (JSONException e) {

        }

        progressView.setVisibility(View.INVISIBLE);
        findViewById(R.id.captions).setVisibility(View.VISIBLE);

    }

    @Override
    public void imageDownloadComplete(float scale_x, float scale_y) {

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
        b.putString("captionImageId", captionMapping.get(v.getId()).get(0));
        b.putString("captionType", captionMapping.get(v.getId()).get(1));
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
