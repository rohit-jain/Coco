package com.palpiction.rohitjain.coco;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by rohitjain on 08/03/16.
 */
public class HelpActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);
        setTitle("Help");
        TextView tv = (TextView) findViewById(R.id.helpTextView);
        tv.setText(Html.fromHtml("<b>How does this work?</b><br/>The main screen contains an image area (the grey box). As you touch different areas of the image, any objects Palpiction recognizes will be read out loud.<br/>"+
        "At any time, double tap outside of the image area (in the background). All undiscovered objects will be read out.<br/>"+
        "Press the “Show Captions” button. You’ll be asked to choose between 3 image captions. Choose the caption that best describes the image.<br/>"+"<br/>"+
        "<b>Nothing is happening. What’s going on?</b><br/>"+ "Palpiction demo requires a working internet connection. Also make sure that your device’s media volume is at an audible level. For turning up the media volume, go to settings > sound and turn up the media volume.<br/>"+"<br/>"+
                "<b>It seems like objects are still being read out after I am done touching?</b></br>"+
                "Depending on what speech speed is selected, there may be a queue of already-touched objects that still need to be read out. Keep trying to figure out where each object is!<br/><br/>"+
                "<b>How am I being scored?</b><br/>" +"Users are scored based on the caption selected in the “Please select a caption for the image you just explored” screen. One of the captions is correct, so try to select the right one!<br/><br/>"+

                "<b>What do these things on the Settings screen mean?</b><br/>"+
                "On the ‘Settings’ screen you can change three things:<br/>"+
        "Username: The username that is used throughout the application<br/>"+
        "Speech Rate: Speech rate at which text is read out<br/>"+
        "Touch Active: Whether the image-area touch-to-speech feature is on. When off, only double-tap will be active<br/>"+"<br/>"+

        "<b>Does Palpiction Demo work with accessibility services?</b><br/>"+
                "Yes! Palpiction Demo is fully compatible with Android’s TalkBack service. Download and enable Talkback and try it out?<br/>"+
                "<br/>"+
        "<b>I’m having trouble swiping or double-tapping the background with TalkBack on. What gives?</b><br/>"+
        "When TalkBack is enabled, swiping is performed a two finger gesture after selecting the image area. Double-tapping the background is performed by double tapping with two finger when off of other areas."+
        "<br/><br/>"+
        "<b>Any other questions?</b><br/>"+
                "Reach out to us at palpiction@gmail.com" ));

    }

}
