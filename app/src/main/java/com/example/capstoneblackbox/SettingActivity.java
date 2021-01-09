package com.example.capstoneblackbox;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivitySettingBinding;

import static com.example.capstoneblackbox.MainActivity.mcontext;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    public static Context stcontext;

    Button chgId;
    Button logout;
    Button myVid;

    public int succeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        stcontext = this;

        chgId = binding.buttonChgId;
        myVid = binding.buttonMyVid;
        logout = binding.buttonLogout;

        myVid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                ((MainActivity) mcontext).connectServerPost
                        .requestGet("http://4982a1961666.ngrok.io/api/full");

            }
        });

    }
}