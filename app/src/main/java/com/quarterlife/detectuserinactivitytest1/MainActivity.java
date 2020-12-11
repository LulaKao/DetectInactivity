package com.quarterlife.detectuserinactivitytest1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {
    public static final long DISCONNECT_TIMEOUT = 1 * 60 * 1000; // set inactivity time : 1 minute
    private boolean isFirstOnResume = true; // detect if the application is onResume() for the first time or not
    private static boolean isVideo = true; // connect API and check if AD is video or not ( true -> video / false -> image )
    private static ImageView ad_img;
    private static FrameLayout ad_video; // RelativeLayout
    private static VideoView videoView; // CustomVideoView
    public static boolean previousPage = false; // default: no previous page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initView
        ad_img = findViewById(R.id.imageView);
        ad_video = findViewById(R.id.videoLayout);
        videoView = findViewById(R.id.videoView);

        // set videoView callback
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);

        // connect API and check AD type
        showAD(); // show AD
    }

    // video onCompletion
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("MediaPlayer","video onCompletion");
    }

    // video onError
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("MediaPlayer","video onError!!!");
        return true;
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
            showAD(); // show AD
        }
    };

    // show AD
    public static void showAD() {
        System.out.println("=== MainActivity showAD() ===");

        // set visibility
        if(!isVideo){ // image
            if(ad_img.getVisibility() != View.VISIBLE) ad_img.setVisibility(View.VISIBLE); // show ad_img
            if(ad_video.getVisibility() != View.GONE) ad_video.setVisibility(View.GONE); // hide ad_video
        } else { // video
            if(ad_video.getVisibility() != View.VISIBLE) ad_video.setVisibility(View.VISIBLE); // show ad_video
            if(ad_img.getVisibility() != View.GONE) ad_img.setVisibility(View.GONE); // hide ad_img
        }

        // set content
        if(!isVideo){ // image
            ad_img.setImageResource(R.drawable.ad_image); // set image resource
            System.out.println("=== MainActivity setImageResource ===");

        } else { // video
            videoView.setVideoPath("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"); // set video path
//            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video)); // set video uri
            videoView.start(); // start video
            System.out.println("=== MainActivity setVideoPath videoView.start() ===");
        }
    }

    // reset timer
    public void resetDisconnectTimer(){
        System.out.println("=== MainActivity resetDisconnectTimer() ===");
        isFirstOnResume = false; // set isFirstLaunch = false

        disconnectHandler.removeCallbacks(disconnectCallback); // remove the old one
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT); // post the new one
    }

    // stop timer
    public void stopDisconnectTimer(){
        System.out.println("=== MainActivity stopDisconnectTimer ===");
        disconnectHandler.removeCallbacks(disconnectCallback); // remove the old one
    }

    // onUserInteraction: if user have any interaction, including touch, slide, click, etc.
    @Override
    public void onUserInteraction(){
        System.out.println("=== MainActivity onUserInteraction ===");

        /*  if user have any interaction,
        *   we will reset the timer */
        resetDisconnectTimer();
    }

    // onResume
    @Override
    public void onResume() {
        super.onResume();

        if(!isFirstOnResume){

            /*  do not reset the timer when the application is onResume() for the first time  */
            if(!previousPage){ // no previous page
                resetDisconnectTimer(); // reset timer
            } else { // from another page
                previousPage = false; // reset previousPage
            }

            /*  do not start video when the application is onResume() for the first time  */
            if(ad_video.getVisibility() == View.VISIBLE){
                videoView.start(); // start video
                System.out.println("=== MainActivity onResume() videoView.start() ===");
            }
        }
    }

    // onStop
    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer(); // stop timer
    }

    // onPause
    @Override
    protected void onPause() {
        super.onPause();
        if(videoView != null && ad_video.getVisibility() == View.VISIBLE) videoView.stopPlayback(); // stop video
    }

    // go to IntroductionActivity
    public void goIntro(View view) {
        startActivity(new Intent(MainActivity.this, IntroductionActivity.class));
    }

    // hide ad_img
    public void imgGone(View view) {
        System.out.println("=== imgGone ===");
        if(ad_img.getVisibility() != View.GONE) ad_img.setVisibility(View.GONE); // hide ad_img
    }

    // hide ad_video
    public void videoGone(View view) {
        System.out.println("=== videoGone ===");
        if(videoView != null) videoView.stopPlayback(); // stop video
        if(ad_video.getVisibility() != View.GONE) ad_video.setVisibility(View.GONE); // hide ad_video
    }
}