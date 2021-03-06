package com.example.rohitjain.coco;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by rohitjain on 05/12/15.
 */

public class SettingsActivity extends AppCompatActivity implements UsernameDialog.NoticeDialogListener, HandleResponse{

    private SeekBar seekBar;
    private CheckBox touchCheckBox;
    TextView tv;
    int currentSpeechRate;
    int changedSpeechRate;
    final Boolean DEFAULT_TOUCH_STATUS = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        findViewById(R.id.usernameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment usernameDialogFragment = new UsernameDialog();
                // Supply username as an argument.
                Bundle args = new Bundle();
                args.putString(MainActivity.USERNAME, getUsername());
                usernameDialogFragment.setArguments(args);

                usernameDialogFragment.show(getSupportFragmentManager(), "username");

            }
        });

        // get textview for username
        tv = (TextView) findViewById(R.id.username);
        tv.setText(getUsername());

        touchCheckBox = (CheckBox) findViewById(R.id.touch_checkbox);
        touchCheckBox.setChecked(isTouchEnabled());

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

    private String getUsername(){
        SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        return settings.getString(MainActivity.USERNAME, MainActivity.DEFAULT_USERNAME);
    }

    @Override
    public void downloadComplete(String output, DownloadImageJson.TaskType task) {
        if(task == DownloadImageJson.TaskType.GET_USER_SCORE) {
            // do nothing for now
        }
    }

    @Override
    public void imageDownloadComplete(float scale_x, float scale_y) {

    }

    @Override
    public void removeFromTtsList(Boundary b) {

    }

    /*
        Set username in shared preference file
         */
    public void setUsername(String output) {
        SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, MODE_PRIVATE);

        settings.edit().putString(MainActivity.USERNAME,output).commit();

    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Dialog dialogView = dialog.getDialog();
        EditText editUsername = (EditText) dialogView.findViewById(R.id.username);
        tv.setText(editUsername.getText().toString());
        setUsername(editUsername.getText().toString());
        String getScoreURL = String.format(getString(R.string.get_score_url),getString(R.string.CURRENT_IP)) + editUsername.getText().toString();
        new DownloadImageJson(this, DownloadImageJson.TaskType.GET_USER_SCORE).execute(getScoreURL);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }


    @Override
    protected void onPause() {
        changeSpeechRate();
        super.onPause();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.touch_checkbox:
                SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
                settings.edit().putBoolean(MainActivity.TOUCH_ENABLED,checked).commit();
                break;
        }
    }

    public boolean isTouchEnabled(){
        SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        return settings.getBoolean(MainActivity.TOUCH_ENABLED, DEFAULT_TOUCH_STATUS);
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
