package com.example.rohitjain.coco;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by rohitjain on 11/10/15.
 */
public class DownloadImageTask extends AsyncTask<String, Void, BitmapFactory.Options> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
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
        bmImage.getLayoutParams().height = result.outHeight;
        bmImage.getLayoutParams().width = result.outWidth;
        bmImage.requestLayout();
    }
}