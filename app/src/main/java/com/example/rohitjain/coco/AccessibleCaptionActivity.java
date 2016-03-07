package com.example.rohitjain.coco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AccessibleCaptionActivity extends AppCompatActivity implements View.OnClickListener, HandleResponse{

    String imageId;
    int captionsUsed, doubleTapUsed;
    CircularProgressView progressView;
    HashMap<Integer, List<String>> captionMapping = new HashMap<Integer, List<String>>();
    final String SHARED_PREFERENCE_FILE = "COCO_PREFERENCES";
    final String USERNAME = "username"; // key for shared pref file


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
        String captionType = captionMapping.get(v.getId()).get(1);
        String captionImageId = captionMapping.get(v.getId()).get(0);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        final String DEFAULT_USERNAME = "daffi";
        final String URL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/recordoutcome_acc";
        nameValuePairs.add(new BasicNameValuePair("username", settings.getString(USERNAME, DEFAULT_USERNAME)));
        nameValuePairs.add(new BasicNameValuePair("image_id", imageId));
        nameValuePairs.add(new BasicNameValuePair("double_used", Integer.toString(doubleTapUsed)));
        nameValuePairs.add(new BasicNameValuePair("captions_used", Integer.toString(captionsUsed)));
        nameValuePairs.add(new BasicNameValuePair("caption_type", captionType));
        nameValuePairs.add(new BasicNameValuePair("caption_image_id", captionImageId));

        new sendPost(nameValuePairs).execute(URL);
        finish();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        settings.edit().putBoolean(MainActivity.LOAD_NEW_IMAGE,true).commit();
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);

    }

    @Override
    public void removeFromTtsList(Boundary b) {

    }

    @Override
    public void setUsername(String output) {

    }
}
