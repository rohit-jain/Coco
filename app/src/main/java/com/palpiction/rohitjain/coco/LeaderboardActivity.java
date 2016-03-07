package com.palpiction.rohitjain.coco;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.palpiction.rohitjain.coco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitjain on 05/12/15.
 */
public class LeaderboardActivity extends AppCompatActivity implements HandleResponse {
    public List<String> scores = new ArrayList<String>();
    public List<String> usernames = new ArrayList<String>();
    String currentUsername;
    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        setTitle("Leaderboard");
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUsername = getIntent().getExtras().getString(MainActivity.USERNAME);

        String getScoreURL = String.format(getString(R.string.get_score_url),getString(R.string.CURRENT_IP)) + currentUsername;
        Log.d("Leaderboard", getScoreURL);
        new DownloadImageJson(this, DownloadImageJson.TaskType.GET_USER_SCORE).execute(getScoreURL);

        final String leaderboardURL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/leaderboard";
        new DownloadImageJson( this, DownloadImageJson.TaskType.GET_LEADERBOARD).execute(leaderboardURL);

        TextView currentUserText = (TextView) findViewById(R.id.currentUsername);
        currentUserText.setText(currentUsername);

        gv = (GridView) findViewById(R.id.contextgrid);

    }

    @Override
    public void downloadComplete(String output, DownloadImageJson.TaskType task) {

        if( task == DownloadImageJson.TaskType.GET_LEADERBOARD ) {
            Log.v("Leaderboard", output);

            try {

                JSONArray objects = new JSONArray(output);
                for (int i = 0; i < objects.length(); i++) {
                    JSONObject curr = new JSONObject(objects.get(i).toString());
                    usernames.add(curr.getString("username"));
                    scores.add(curr.getString("score"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            gv.setAdapter(new CustomLeaderboardAdapter(this, scores, usernames, currentUsername));
        }
        else if(task == DownloadImageJson.TaskType.GET_USER_SCORE) {
            Log.v("Leaderboard",output);

            try {
                JSONObject scoreJson = new JSONObject(output);
                String currentUserScore = scoreJson.getString("score");
                TextView currentUserScoreText = (TextView) findViewById(R.id.currentUserScore);
                currentUserScoreText.setText(currentUserScore);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void imageDownloadComplete(float scale_x, float scale_y) {

    }

    @Override
    public void removeFromTtsList(Boundary b) {

    }

    @Override
    public void setUsername(String output) {

    }

}
