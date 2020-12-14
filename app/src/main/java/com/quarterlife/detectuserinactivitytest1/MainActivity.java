package com.quarterlife.detectuserinactivitytest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quarterlife.detectuserinactivitytest1.Fragment.HomeFragment;
import com.quarterlife.detectuserinactivitytest1.Fragment.SettingFragment;
import com.quarterlife.detectuserinactivitytest1.Fragment.UserFragment;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public static final long DISCONNECT_TIMEOUT = 1 * 60 * 1000; // set inactivity time : 1 minute
    private boolean isFirstOnResume = true; // detect if the application is onResume() for the first time or not
    private static boolean isVideo = true; // connect API and check if AD is video or not ( true -> video / false -> image )
    private static ImageView ad_img;
    private static FrameLayout ad_video; // RelativeLayout
    private static VideoView videoView; // CustomVideoView
    public static boolean previousPage = false; // default: no previous page
    public static String PACKAGE_NAME;
    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;
    private HomeFragment homeFragment = null;
    private UserFragment userFragment = null;
    private SettingFragment settingFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get package name
        PACKAGE_NAME = getPackageName();

        // initial View
        ad_img = findViewById(R.id.imageView);
        ad_video = findViewById(R.id.videoLayout);
        videoView = findViewById(R.id.videoView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set videoView callback
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnPreparedListener(this);

        // connect API and check AD type
        showAD(); // show AD

        // setOnNavigationItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // initial Fragment
        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        settingFragment = new SettingFragment();

        // set default fragment : HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    // set BottomNavigationView
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // set selected fragment
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = homeFragment;
                            break;
                        case R.id.nav_user:
                            selectedFragment = userFragment;
                            break;
                        case R.id.nav_setting:
                            selectedFragment = settingFragment;
                            break;
                    }

                    // replace selected fragment on fragment container
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    // video onCompletion
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("MediaPlayer","MainActivity video onCompletion");
    }

    // video onError
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("MediaPlayer","MainActivity video onError!!!");
        return true;
    }

    // video onPrepared
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true); // set looping
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
//            videoView.setVideoPath("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"); // set video path : URL
            videoView.setVideoPath("android.resource://" + PACKAGE_NAME + "/" + R.raw.vertical_video); // set video path : local video
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
        HomeFragment.startHomeVideo(); // start HomeFragment video
    }
}