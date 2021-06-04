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

        btnup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToAlbum();
            }
        });

        btndwn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((MainActivity)MainActivity.mcontext).connectServerPost
                        .requestVideoCnt("http://3.35.118.6/api/edited/count");
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
            Uri uri2;
            try {
                uri2 = data.getData();
                if (uri2.toString().contains("video")) {
                    UriToPath uri2path = new UriToPath();
                    videopath = uri2path.getPath(p2context, uri2);
                    popup_to_abup();
                }
            } catch (Exception e) {
                Intent intent = new Intent(Popup2Activity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    public void popup_to_ab() {
        Intent intent = new Intent(Popup2Activity.this, AbnormalDownloadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void popup_to_abup() {
        Intent intent = new Intent(Popup2Activity.this, AbnormUploadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}