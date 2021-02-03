package com.example.capstoneblackbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.capstoneblackbox.MainActivity.mcontext;

public class AbnormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abnormal);

        ((MainActivity) mcontext).connectServerPost
                .requestVideoGet("http://b3229d98848b.ngrok.io/output", "/storage/emulated/0/Download/");
    }
}