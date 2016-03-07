package com.example.rohitjain.coco;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitjain on 05/12/15.
 */
public class sendPost extends AsyncTask<String, Void, String> {

    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


    public sendPost(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }


    @Override
    protected String doInBackground(String... uri) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(uri[0]);
        HttpResponse httpresponse = null;
        HttpEntity httpEntity = null;

        String response=null;
        try {
            // Add your data
            httppost.setEntity(new UrlEncodedFormEntity(this.nameValuePairs));

            // Execute HTTP Post Request
            httpresponse = httpclient.execute(httppost);
            httpEntity = httpresponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.v("POST",response.toString());

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return response.toString();
    }



    @Override
    protected void onPostExecute(String output) {

    }
}