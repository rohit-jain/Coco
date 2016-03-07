package com.example.rohitjain.coco;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.InputStream;

/**
 * Created by rohitjain on 11/10/15.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage, overlayImage;
    CircularProgressView progressView;
    Boolean showImage;
    int imageHeight, imageWidth, screenWidth, screenHeight;
    // callback
    HandleResponse delegate=null;

    public DownloadImageTask(HandleResponse delegate, ImageView bmImage, ImageView overlayImage, CircularProgressView progressView, Boolean showImage, Point size, int dpi) {
        this.bmImage = bmImage;
        this.overlayImage = overlayImage;
        this.progressView = progressView;
        this.showImage = showImage;
        this.screenWidth = size.x;
        this.screenHeight = (int)(size.y*0.55);
        this.delegate = delegate;
    }

    public DownloadImageTask(ImageView bmImage, CircularProgressView progressView, Boolean showImage) {
        this.bmImage = bmImage;
        this.overlayImage = null;
        this.progressView = progressView;
        this.showImage = showImage;
    }

    protected void onPreExecute(){
        this.bmImage.setImageBitmap(null);
        if (this.overlayImage != null)
            this.overlayImage.setImageBitmap(null);
        this.progressView.startAnimation();
        this.progressView.setVisibility(View.VISIBLE);
        //this.mDialog = ProgressDialog.show(this.activityContext, "Loading", "Wait while loading...");
    }

    protected Bitmap doInBackground(String... urls) {

        String urldisplay = urls[0];
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap mIcon11 = null, bitmap2 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in, null, options);
            this.imageHeight = options.outHeight;
            this.imageWidth = options.outWidth;
            Log.v("Download", options.outHeight + " " + options.outWidth);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap resultImage) {
        this.progressView.setVisibility(View.INVISIBLE);
//            Log.d("Download", scale_x+" "+scale_y);
//            Log.d("Download", this.screenHeight+" "+this.screenWidth);

        if (this.showImage == true) {
//            set image width and height
//            this.bmImage.getLayoutParams().height = this.imageHeight;
//            this.bmImage.getLayoutParams().width = this.imageWidth;
            this.bmImage.requestLayout();
            this.bmImage.setImageBitmap(resultImage);
//            this.delegate.imageDownloadComplete(scale_x, scale_y);
        }
        else {
//            this.bmImage.getLayoutParams().height = this.imageHeight;
//            this.bmImage.getLayoutParams().width = this.imageWidth;
            float scale_x = this.screenWidth/(float)this.imageWidth;
            float scale_y = this.screenHeight/(float)this.imageHeight;

            this.bmImage.requestLayout();
            this.delegate.imageDownloadComplete(scale_x, scale_y);
            if(this.overlayImage != null) {
//                this.overlayImage.getLayoutParams().height = this.imageHeight;
//                this.overlayImage.getLayoutParams().width = this.imageWidth;
                this.overlayImage.requestLayout();
            }

        }
    }
}