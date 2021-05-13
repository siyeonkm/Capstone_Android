package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivityAbnormUploadBinding;

public class AbnormUploadActivity extends AppCompatActivity {
    public static Context abupcontext;
    ActivityAbnormUploadBinding binding;
    String date = "2021-01-01 12:30:01";
    String size = "1000";
    String path = "/input/test.mp4";
    int user_id = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbnormUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        abupcontext = this;

        String video_path = ((Popup2Activity)Popup2Activity.p2context).videopath;

        ((MainActivity)MainActivity.mcontext).connectServerPost
                .requestPost("http://3.34.148.201/api/full", video_path, path, size, date, user_id);
    }

    public void fromAbUptoHomeActivity() {
        Intent intent = new Intent(AbnormUploadActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}