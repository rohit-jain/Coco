package com.example.rohitjain.coco;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rohitjain on 20/10/15.
 */
public class DownloadImageJson extends AsyncTask<String, Void, String> {


    public enum TaskType{
        DOWNLOAD_IMAGE, GET_USERNAME, GET_LEADERBOARD, GET_USER_SCORE;
    }

    // callback
    HandleResponse delegate=null;
    TaskType requestType = null;


    public DownloadImageJson(HandleResponse delegate, TaskType requestType){
        this.delegate = delegate;
        this.requestType = requestType;
    }

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
        if(this.requestType==TaskType.DOWNLOAD_IMAGE) {
            this.delegate.downloadComplete(output, this.requestType);
        }
        else if(this.requestType==TaskType.GET_USERNAME){
            this.delegate.setUsername("user"+output);
        }
        else if(this.requestType==TaskType.GET_LEADERBOARD) {
            this.delegate.downloadComplete(output,this.requestType);
        }
        else if(this.requestType==TaskType.GET_USER_SCORE) {
            this.delegate.downloadComplete(output,this.requestType);
        }
    }
}

