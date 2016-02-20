package com.example.rohitjain.coco;

/**
 * Created by rohitjain on 19/02/16.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.Locale;

public class MyAccessibilityService extends AccessibilityService {
//    TextToSpeech tts;

    @Override
    public void onCreate() {
        Log.d("Access","creating");

//        getServiceInfo().flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.d("access", "We are connected!");
//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
//        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
//        info.notificationTimeout = 100;
//        info.packageNames = null;
//        info.feedbackType = AccessibilityServiceInfo.DEFAULT;
//        this.setServiceInfo(info);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        Log.d("Access",eventType+"");
        String eventText = null;
        switch(eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "Focused: ";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "Focused: ";
                break;
        }

        eventText = "yo";
        Log.d("Access",eventText);
        // Do something nifty with this text, like speak the composed string
        // back to the user.
//        tts = new TextToSpeech(this, this);
//        tts.speak( eventText, TextToSpeech.QUEUE_FLUSH, null);

    }


    @Override
    public void onInterrupt() {
    }


//    @Override
//    public void onInit(int status) {
//        if(status != TextToSpeech.ERROR) {
//            tts.setLanguage(Locale.US);
//            tts.setSpeechRate(getSpeechRate());
//        }

//    }

    public Float getSpeechRate(){
        SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        Log.v(MainActivity.TTS_TAG, "speech rate "+settings.getFloat(MainActivity.SPEECH_RATE, MainActivity.DEFAULT_SPEECH_RATE));

        return settings.getFloat(MainActivity.SPEECH_RATE, MainActivity.DEFAULT_SPEECH_RATE);
    }

}