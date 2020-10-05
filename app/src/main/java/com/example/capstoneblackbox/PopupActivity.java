package com.example.capstoneblackbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
    }

    public void onButton_abnormalDetectClicked(View v){
        Intent intent = new Intent(PopupActivity.this, AbnormalActivity.class );
        startActivity(intent);
    }

    public void onButton_impactDetectionClicked(View v){
        Intent intent = new Intent(PopupActivity.this, ImpactActivity.class );
        startActivity(intent);
    }

    public void onButton_userEditClicked(View v){
        Intent intent = new Intent(PopupActivity.this, UserEditActivity.class );
        startActivity(intent);
    }
}