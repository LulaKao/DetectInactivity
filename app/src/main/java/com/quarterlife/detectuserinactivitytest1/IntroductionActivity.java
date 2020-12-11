package com.quarterlife.detectuserinactivitytest1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class IntroductionActivity extends AppCompatActivity {
    public static final long DISCONNECT_TIMEOUT = 1 * 60 * 1000; // set inactivity time : 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
    }

    // create handler
    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };


    // create callback
    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            finish(); // finish this Activity
            MainActivity.showAD(); // show AD
            MainActivity.previousPage = true; // set MainActivity.previousPage = true
        }
    };

    // reset timer
    public void resetDisconnectTimer(){
        System.out.println("=== IntroductionActivity resetDisconnectTimer() ===");

        disconnectHandler.removeCallbacks(disconnectCallback); // remove the old one
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT); // post the new one
    }

    // stop timer
    public void stopDisconnectTimer(){
        System.out.println("=== IntroductionActivity stopDisconnectTimer ===");
        disconnectHandler.removeCallbacks(disconnectCallback); // remove the old one
    }

    // onUserInteraction: if user have any interaction, including touch, slide, click, etc.
    @Override
    public void onUserInteraction(){
        System.out.println("=== IntroductionActivity onUserInteraction ===");

        /*  if user have any interaction,
         *   we will reset the timer */
        resetDisconnectTimer();
    }

    // onResume
    @Override
    public void onResume() {
        super.onResume();

        resetDisconnectTimer();
    }

    // onStop
    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer(); // stop timer
    }
}