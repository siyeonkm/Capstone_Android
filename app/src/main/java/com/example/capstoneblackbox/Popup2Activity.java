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

import com.example.capstoneblackbox.databinding.ActivityPopup2Binding;

public class Popup2Activity extends Activity {
    ActivityPopup2Binding binding;
    public static Context p2context;

    Button btnup;
    Button btndwn;

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

        binding = ActivityPopup2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        p2context = this;

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;

        btnup = binding.buttonUpload;
        btndwn = binding.buttonDownload;

        goToAlbum();

        btnup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((MainActivity)MainActivity.mcontext).connectServerPost
                        .requestPost("http://a7c2385c8c03.ngrok.io/api/full", videopath, path, size, date, user_id);
            }
        });

        btndwn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DownloadScreenActivity();
            }
        });

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
                videopath = uri2path.getPath(p2context, uri2);
            }

        }
    }

    public void DownloadScreenActivity() {
        Intent intent = new Intent(Popup2Activity.this, AbnormalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}