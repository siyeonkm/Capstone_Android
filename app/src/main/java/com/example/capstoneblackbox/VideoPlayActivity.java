package com.example.capstoneblackbox;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

    VideoView videoView;
    ImageButton imageButton;
    String videoPath;
    Bitmap bitmap;
    FrameLayout thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);

        videoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        String videoName = intent.getExtras().getString("videoName");
        //bitmap = (Bitmap)intent.getExtras().get("bitmap");
        //videoView.setBackground(bitmap);

        //thumbnail = findViewById(R.id.thumbnail);
        //thumbnail.setBackground(bitmap);

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        videoPath = sdcard + File.separator + videoName + ".mp4";

        videoView.setVideoPath(videoPath);


       final MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        videoView.requestFocus();
        videoView.start();
        //videoView.postDelayed(new Runnable() { @Override public void run() { mc.show(0); } }, 100);

        /*
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //videoView.setVideoURI(getUriFromPath(videoPath));

                videoView.requestFocus();
                videoView.start();
            }
        });

         */
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