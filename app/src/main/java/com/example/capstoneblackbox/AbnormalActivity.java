package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivityAbnormalBinding;

import static com.example.capstoneblackbox.MainActivity.mcontext;

public class AbnormalActivity extends AppCompatActivity {
    public static Context abcontext;
    ActivityAbnormalBinding binding;
    String vidpath;
    Uri viduri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAbnormalBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        abcontext = this;

        ((MainActivity) mcontext).connectServerPost
                .requestVideoGet("http://42cdf7866d6b.ngrok.io/output");
    }

    public void goToAlbum() {
        Uri targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String targetDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath().toString() + "/MagicBox";
        targetUri = targetUri.buildUpon().appendQueryParameter("bucketId", String.valueOf(targetDir.toLowerCase().hashCode())).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, targetUri);
        intent.setType("video/*");
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            viduri = data.getData();
            if (viduri.toString().contains("video")) {
                UriToPath uri2path = new UriToPath();
                vidpath = uri2path.getPath(abcontext, viduri);
            }
        }
    }
}