package com.quarterlife.detectuserinactivitytest1.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;
import com.quarterlife.detectuserinactivitytest1.IntroductionActivity;
import com.quarterlife.detectuserinactivitytest1.MainActivity;
import com.quarterlife.detectuserinactivitytest1.R;

public class HomeFragment extends Fragment implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private Button btn_go_intro;
    private static VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initial View
        btn_go_intro = getView().findViewById(R.id.btn_go_intro);
        videoView = getView().findViewById(R.id.videoView);

        // btn_go_intro setOnClickListener
        btn_go_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), IntroductionActivity.class)); // go to IntroductionActivity
            }
        });

        // set videoView callback
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnPreparedListener(this);
    }

    // start video
    public static void startHomeVideo() {
        videoView.setVideoPath("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.horizontal_video); // set video path : local video
        videoView.start(); // start video
    }

    // video onCompletion
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("MediaPlayer","HomeFragment video onCompletion");
    }

    // video onError
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("MediaPlayer","HomeFragment video onError!!!");
        return true;
    }

    // video onPrepared
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true); // set looping
    }
}