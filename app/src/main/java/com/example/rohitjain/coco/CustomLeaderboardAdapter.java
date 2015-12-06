package com.example.rohitjain.coco;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitjain on 05/12/15.
 */
public class CustomLeaderboardAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater=null;
    public static List<String> scoreValues = new ArrayList<String>();
    public static List<String>  usernameValues = new ArrayList<String>();

    public CustomLeaderboardAdapter(Activity leaderboardActivity, List<String> listScoreValues, List<String> listUsernameValues) {
        context = leaderboardActivity;
        usernameValues = listUsernameValues;
        scoreValues = listScoreValues;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.usernameValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView username;
        TextView score;
        TextView rank;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)

    {
        final Holder holder=new Holder();
        final View rowView = inflater.inflate(R.layout.leaderboard_card, null);
        holder.username = (TextView) rowView.findViewById(R.id.firstLine);
        holder.score = (TextView) rowView.findViewById(R.id.secondLine);
        holder.rank = (TextView) rowView.findViewById(R.id.rank);

        holder.img = (ImageView) rowView.findViewById(R.id.icon);
        Log.v("Leaderboard", this.usernameValues.get(position));
        holder.username.setText(this.usernameValues.get(position));
        holder.score.setText(this.scoreValues.get(position));
        int userRank = position+1;
        holder.rank.setText(userRank + "");
        return rowView;
    }

}