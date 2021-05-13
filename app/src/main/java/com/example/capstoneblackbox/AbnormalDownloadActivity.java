package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivityAbnormalDownloadBinding;

import static com.example.capstoneblackbox.MainActivity.mcontext;

public class AbnormalDownloadActivity extends AppCompatActivity {
    public static Context abcontext;
    ActivityAbnormalDownloadBinding binding;
    private long backKeyPressedTime = 0;
    String vidpath;
    Uri viduri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAbnormalDownloadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        abcontext = this;

        ((MainActivity) mcontext).connectServerPost
                .requestVideoGet("http://3.34.148.201/output");
    }

    public void fromAbtoHomeActivity() {
        Intent intent = new Intent(AbnormalDownloadActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}