package com.example.capstoneblackbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.capstoneblackbox.databinding.ActivityPopupBinding;

public class PopupActivity extends Activity {
    ActivityPopupBinding binding;
    public static Context pcontext;

    Button btnAbnorm;
    Button btnImpact;
    Button btnUser;

    private static final int PICK_FROM_ALBUM = 1;

    String date = "2021-01-01 12:30:01";
    String size = "1000";
    String path = "/input/test.mp4";
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

        goToAlbum();

        btnAbnorm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

               ((MainActivity)MainActivity.mcontext).connectServerPost
                        .requestPost("http://b3229d98848b.ngrok.io/api/full", videopath, path, size, date, user_id);
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

    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {

            Uri uri2 = data.getData();

            if (uri2.toString().contains("video")) {
                UriToPath uri2path = new UriToPath();
                videopath = uri2path.getPath(pcontext, uri2);
            }

        }
    }

}