package com.example.capstoneblackbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.capstoneblackbox.databinding.ActivityPopupBinding;

import static com.example.capstoneblackbox.MainActivity.mcontext;

public class PopupActivity extends Activity {
    ActivityPopupBinding binding;
    public static Context pcontext;

    Button btnAbnorm;
    Button btnImpact;
    Button btnUser;

    private static final int PICK_FROM_ALBUM = 1;

    String date = "2021-01-01 12:30:01";
    String size = "1000";
    String path = "/uploads/test.mp4";
    int user_id = 2;
    String videopath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = ActivityPopupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        pcontext = this;

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;

        btnAbnorm = binding.buttonAbnormalDetect;
        btnImpact = binding.button2;
        btnUser = binding.button3;

        btnAbnorm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                videopath = ((HomeActivity)HomeActivity.hcontext).videopath;

                ((MainActivity)MainActivity.mcontext).connectServerPost
                        .requestPost("http://b049b8cfa4d4.ngrok.io/api/input", videopath, path, size, date, user_id);

                ((MainActivity) mcontext).connectServerPost
                        .requestVideoGet("http://b049b8cfa4d4.ngrok.io/output", "/storage/emulated/0/MagicBox/");
            }
        });

        btnImpact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupActivity.this, ImpactActivity.class );
                startActivity(intent);
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupActivity.this, UserEditActivity.class );
                startActivity(intent);
            }
        });
    }

    public void goAbnormAct() {
        Intent intent = new Intent(PopupActivity.this, AbnormalActivity.class );
        startActivity(intent);
    }

}