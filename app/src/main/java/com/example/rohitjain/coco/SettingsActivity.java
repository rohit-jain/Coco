package com.example.rohitjain.coco;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;


/**
 * Created by rohitjain on 05/12/15.
 */

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBar;
    int currentSpeechRate;
    int changedSpeechRate;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekBar = (SeekBar) findViewById(R.id.speechRateSeekBar);
        currentSpeechRate = Math.round(getSpeechRate());
        seekBar.setProgress(currentSpeechRate);
        changedSpeechRate = currentSpeechRate;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                changedSpeechRate = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    @Override
    protected void onPause() {
        changeSpeechRate();
        super.onPause();
    }

    Float getSpeechRate(){
        final Float DEFAULT_SPEECH_RATE = new Float(3.0);
        SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        return settings.getFloat(MainActivity.SPEECH_RATE, DEFAULT_SPEECH_RATE);
    }

    void changeSpeechRate(){
        SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        settings.edit().putFloat(MainActivity.SPEECH_RATE, (float)changedSpeechRate).commit();
    }

}
