package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    public static Context hcontext;
    private long backKeyPressedTime = 0;
    private Toast toast;

    ImageButton btnvideo;
    ImageButton btncrop;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        hcontext = this;

        btnvideo = binding.videoButton;
        btncrop = binding.cropButton;
        

        btnvideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //기본 카메라 연결
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                   startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }

                //Intent intent = new Intent(HomeActivity.this, RecordActivity.class );
                //startActivity(intent);
            }
        });

        btncrop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PopupActivity.class );
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}