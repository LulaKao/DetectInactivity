package com.quarterlife.detectuserinactivitytest1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final long DISCONNECT_TIMEOUT = 1 * 60 * 1000; // set inactivity time : 1 minute
    private boolean isFirstOnResume = true; // detect if the application is onResume() for the first time or not
    private ImageView ad_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initView
        ad_img = findViewById(R.id.imageView);
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
            ad_img.setVisibility(View.VISIBLE); // show ad_img
        }
    };

    // reset timer
    public void resetDisconnectTimer(){
        System.out.println("=== EWT resetDisconnectTimer() ===");
        isFirstOnResume = false; // set isFirstLaunch = false

        disconnectHandler.removeCallbacks(disconnectCallback); // remove the old one
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT); // post the new one
    }

    // stop timer
    public void stopDisconnectTimer(){
        System.out.println("=== EWT stopDisconnectTimer ===");
        disconnectHandler.removeCallbacks(disconnectCallback); // remove the old one
    }

    // onUserInteraction: if user have any interaction, including touch, slide, click, etc.
    @Override
    public void onUserInteraction(){
        System.out.println("=== EWT onUserInteraction ===");

        /*  if user have any interaction,
        *   we will reset the timer */
        resetDisconnectTimer();
    }

    // onResume
    @Override
    public void onResume() {
        super.onResume();

        /*  do not reset the timer when the application is onResume() for the first time  */
        if(!isFirstOnResume) resetDisconnectTimer();
    }

    // onStop
    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer(); // stop timer
    }

    // go to IntroductionActivity
    public void goIntro(View view) {
//        startActivity(new Intent(MainActivity.this, IntroductionActivity.class));
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }

    // hide ad_img
    public void imgGone(View view) {
        ad_img.setVisibility(View.GONE);
    }
}