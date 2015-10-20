package com.example.rohitjain.coco;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.InputStream;

/**
 * Created by rohitjain on 11/10/15.
 */
public class DownloadImageTask extends AsyncTask<String, Void, BitmapFactory.Options> {
    ImageView bmImage;
    Context activityContext;
    CircularProgressView progressView;
//    private ProgressDialog mDialog;


    public DownloadImageTask(ImageView bmImage, CircularProgressView progressView) {
        this.bmImage = bmImage;
//        this.activityContext = c;
        this.progressView = progressView;
    }

    protected void onPreExecute(){
        this.bmImage.setImageBitmap(null);
        this.progressView.startAnimation();
        this.progressView.setVisibility(View.VISIBLE);

        //this.mDialog = ProgressDialog.show(this.activityContext, "Loading", "Wait while loading...");
    }

    protected BitmapFactory.Options doInBackground(String... urls) {
        String urldisplay = urls[0];
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in, null, options);
            Log.v("Download", options.outHeight + " " + options.outWidth);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return options;
    }

    protected void onPostExecute(BitmapFactory.Options result) {
//        bmImage.setImageBitmap(result);
//        bmImage.setVisibility(View.INVISIBLE);
        this.progressView.setVisibility(View.INVISIBLE);
        this.bmImage.getLayoutParams().height = result.outHeight;
        this.bmImage.getLayoutParams().width = result.outWidth;
        this.bmImage.requestLayout();
    }
}