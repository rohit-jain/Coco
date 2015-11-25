package com.example.rohitjain.coco;

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

    // callback
    HandleResponse delegate=null;

    public DownloadImageJson(HandleResponse delegate){
        this.delegate = delegate;
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
        this.delegate.downloadComplete(output);
    }
}

