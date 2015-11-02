package com.example.rohitjain.coco;

import android.speech.tts.UtteranceProgressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitjain on 01/11/15.
 */
public class TextSpeechHandler extends UtteranceProgressListener {

    List<Boundary> boundaryList = new ArrayList<Boundary>();

    TextSpeechHandler(List<Boundary> boundaryList){
        this.boundaryList = boundaryList;
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
        MainActivity.removeFromTtsList(boundaryList.get(Integer.parseInt(utteranceId)));
    }

    @Override
    public void onError(String utteranceId) {

    }
}
