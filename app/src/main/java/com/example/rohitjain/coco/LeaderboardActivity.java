package com.example.rohitjain.coco;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rohitjain on 05/12/15.
 */
public class LeaderboardActivity extends AppCompatActivity implements HandleResponse {
    public static List<String> scores = new ArrayList<String>();
    public static List<String> usernames = new ArrayList<String>();
    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        final String leaderboardURL = "http://"+ getString(R.string.CURRENT_IP) +"/experiment/leaderboard";
        new DownloadImageJson( this, DownloadImageJson.TaskType.GET_LEADERBOARD).execute(leaderboardURL);
        gv = (GridView) findViewById(R.id.contextgrid);

    }

    @Override
    public void downloadComplete(String output) {
        String jsonString;

        jsonString = output;
        Log.v("Leaderboard", jsonString);

        try {
            JSONArray objects = new JSONArray(jsonString);
            for (int i = 0; i < objects.length(); i++)
            {
                JSONObject curr = new JSONObject(objects.get(i).toString());
                usernames.add(curr.getString("username"));
                scores.add(curr.getString("score"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        gv.setAdapter(new CustomLeaderboardAdapter(this, scores, usernames));



    }

    @Override
    public void removeFromTtsList(Boundary b) {

    }

    @Override
    public void setUsername(String output) {

    }

}
