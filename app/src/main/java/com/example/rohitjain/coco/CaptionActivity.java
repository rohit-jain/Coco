package com.example.rohitjain.coco;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

public class CaptionActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        // set an exit transition
        getWindow().setExitTransition(new Explode());

        FloatingActionButton nextButton = (FloatingActionButton)findViewById(R.id.next);
        FloatingActionButton prevButton = (FloatingActionButton)findViewById(R.id.prev);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.next){
            Intent intent = new Intent(CaptionActivity.this, ImageActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else if(v.getId() == R.id.prev){

        }

    }
}
