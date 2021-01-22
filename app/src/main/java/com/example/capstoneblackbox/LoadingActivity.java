package com.example.capstoneblackbox;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class LoadingActivity extends AppCompatActivity {
    ImageView imgGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        imgGif = findViewById(R.id.imgGif);

        Glide.with( getApplicationContext() )
                .asGif()    // GIF 로딩
                .load( R.drawable.loading )
                .diskCacheStrategy( DiskCacheStrategy.RESOURCE )
                .into( imgGif );

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler() , 3000);
        // 3초 후에 splashhandler 실행

    }

    private class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), GalleryActivity.class)); // 로딩이 끝난후 이동할 Activity
            LoadingActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoadingActivity.this, RecordActivity.class);
        startActivity(intent);
    }

}