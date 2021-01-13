package com.example.capstoneblackbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ImpactActivity extends AppCompatActivity implements AutoPermissionsListener {
    private static final int PICK_FROM_ALBUM = 1;
    private String videopath;
    public static Context hcontext;
    private DBOpenHelper mDBopenHelper;
    private ArrayList<Long> arr = new ArrayList<>();
    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impact);
        hcontext = this;
        mDBopenHelper = new DBOpenHelper(this);
        mDBopenHelper.open();
        mDBopenHelper.create();

        AutoPermissions.Companion.loadAllPermissions(this, 101);
        goToAlbum();
    }

    public void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void findVideoFromDB(String videopath){
        StringTokenizer st = new StringTokenizer(videopath,"/");
        String string="";

        while(st.hasMoreTokens()){
            string = st.nextToken();
            Log.d("토큰","토큰 발생"+ string);
            if(string.contains(".mp4")){
                string = string.substring(0,string.indexOf("."));
                Log.d("토큰","토큰 발생-자르기"+ string);
            }

        }
        Cursor iCursor = mDBopenHelper.selectColumns();
        while(iCursor.moveToNext()){
            String videoName = iCursor.getString(iCursor.getColumnIndex(DataBases.CreateDB.VideoName));
            long impactTime = iCursor.getLong(iCursor.getColumnIndex(DataBases.CreateDB.ImpactTime));

            if(videoName.equals(string)){
                arr.add(impactTime);
                Log.d("일치","일치 시간 "+ impactTime);
                //Toast.makeText(this, "충격시간 "+impactTime, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {

            Uri uri2 = data.getData();

            if (uri2.toString().contains("video")) {
                UriToPath uri2path = new UriToPath();
                videopath = uri2path.getPath(hcontext, uri2);
                findVideoFromDB(videopath);
                Toast.makeText(this, "경로 "+videopath, Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }

    @Override
    public void onBackPressed() {
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}