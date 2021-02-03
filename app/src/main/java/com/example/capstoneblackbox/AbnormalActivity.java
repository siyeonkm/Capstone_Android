package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
                .requestVideoGet("http://3b9695b92f5e.ngrok.io/output");
    }

    /*public void goToAlbum() {
        Uri targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String targetDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).toString() + "/MagicBox";
        targetUri = targetUri.buildUpon().appendQueryParameter("bucketId", String.valueOf(targetDir.toLowerCase().hashCode())).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, targetUri);
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
    }*/
    public void fromAbtoHomeActivity() {
        Intent intent = new Intent(AbnormalActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}