package com.example.capstoneblackbox;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

    VideoView videoView;
    String videoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);

        videoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        String videoName = intent.getExtras().getString("videoName");

        if(!videoName.contains(".mp4")) {
            String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
            videoPath = sdcard + File.separator + "Download/MagicBox/"+ videoName + ".mp4";
        }
        else {
            String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath().toString() + "/MagicBoxAbnormal";
            videoPath = dir + "/" + videoName;
        }

        videoView.setVideoPath(videoPath);

       final MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        videoView.requestFocus();
        videoView.start();


    }



/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Video video = list.get(requestCode);
        String videoName = video.getVideoName();

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        videoPath = sdcard + File.separator + videoName + ".mp4";
        Log.d("videopath","videopath : "+videoPath);
    }

 */
}