package com.quarterlife.detectuserinactivitytest1.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.dueeeke.videoplayer.player.VideoView;
import com.quarterlife.detectuserinactivitytest1.IntroductionActivity;
import com.quarterlife.detectuserinactivitytest1.MainActivity;
import com.quarterlife.detectuserinactivitytest1.R;

public class HomeFragment extends BaseFragment {
    private Button btn_go_intro;
    public static VideoView home_videoView;

    //========= onCreateView START =========//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    //========= onCreateView END =========//

    //========= onFragmentFirstVisible START =========//
    @Override
    protected void onFragmentFirstVisible() {
        home_videoView = getView().findViewById(R.id.videoView); // initial videoView
    }
    //========= onFragmentFirstVisible END =========//

    //========= onFragmentVisibleChange START =========//
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible == true) {    /** fragment invisible --> visible  */
            if(home_videoView != null && home_videoView.getCurrentPlayState() == VideoView.STATE_PAUSED) home_videoView.resume(); // resume video

        } else {    /** fragment visible --> invisible  */
            if(home_videoView != null) home_videoView.pause(); // pause video
        }
    }
    //========= onFragmentVisibleChange END =========//

    //========= onViewCreated START =========//
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // btn_go_intro setOnClickListener
        btn_go_intro = getView().findViewById(R.id.btn_go_intro);
        btn_go_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), IntroductionActivity.class)); // go to IntroductionActivity
            }
        });
    }
    //========= onViewCreated END =========//

    //========= start HomeFragment video START =========//
    public static void startHomeVideo() {
        home_videoView.release(); // release video
        home_videoView.setUrl("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.horizontal_video); // set local video path
        home_videoView.start(); // start video
    }
    //========= start HomeFragment video END =========//

    //========= onDestroy START =========//
    @Override
    public void onDestroy() {
        if(home_videoView != null) home_videoView.release(); // release video
        super.onDestroy();
    }
    //========= onDestroy END =========//

    //========= onPause START =========//
    @Override
    public void onPause() {
        if(home_videoView != null) home_videoView.pause(); // pause video
        super.onPause();
    }
    //========= onPause END =========//

    //========= onResume START =========//
    @Override
    public void onResume() {
        if(home_videoView != null) home_videoView.resume(); // resume video
        super.onResume();
    }
    //========= onResume END =========//
}