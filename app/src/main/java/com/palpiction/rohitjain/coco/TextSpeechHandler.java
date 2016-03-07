package com.palpiction.rohitjain.coco;

import android.speech.tts.UtteranceProgressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitjain on 01/11/15.
 */
public class TextSpeechHandler extends UtteranceProgressListener {

    List<Boundary> boundaryList = new ArrayList<Boundary>();
    HandleResponse delegate=null;


    TextSpeechHandler(List<Boundary> boundaryList, HandleResponse delegate){

        this.boundaryList = boundaryList;
        this.delegate = delegate;
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
        this.delegate.removeFromTtsList(boundaryList.get(Integer.parseInt(utteranceId)));
    }

    @Override
    public void onError(String utteranceId) {

    }
}
