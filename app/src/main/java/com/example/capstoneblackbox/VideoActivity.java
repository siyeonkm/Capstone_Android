package com.example.capstoneblackbox;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//설정창에서 내 비디오 보기 클릭하면 비디오 받아서 갤러리에 저장하고
//다 저장하면 이 액티비티로 넘어와서 보여줄 예정
public class VideoActivity extends AppCompatActivity {
    static Context vidcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
    }
}