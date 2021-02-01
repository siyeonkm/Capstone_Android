package com.example.capstoneblackbox;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstoneblackbox.databinding.ActivityHomeBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    public static Context hcontext;
    private long backKeyPressedTime = 0;
    private Toast toast;

    private static final int PICK_FROM_ALBUM = 1;

    ImageButton btnvideo;
    ImageButton btncrop;
    ImageButton btnsetting;

    String date = "2021-01-01 12:30:01";
    String size = "1000";
    String path = "/uploads/test.mp4";
    int user_id = 2;
    String videopath;
    private final int MT_PERMISSION_REQUEST_CODE = 1001;
    private final int AUDIO_PERMISSION = 1002;
    private final int READ_PERMISSION = 1003;
    private final int WRITE_PERMISSION = 1004;
    private final int MY_PERMISSIONS_REQUEST_MULTI = 1005;
    ArrayList<String> permissions;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        hcontext = this;

        btnvideo = binding.videoButton;
        btncrop = binding.cropButton;
        btnsetting = binding.buttonSetting;

        //tedPermission();
        //goToAlbum();

        btnvideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

               // ((MainActivity)MainActivity.mcontext).connectServerPost
                //        .requestPost("http://b049b8cfa4d4.ngrok.io/api/input", videopath, path, size, date, user_id);

                //기본 카메라 연결
                /*Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                   startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }*/

                final int MY_PERMISSION_REQUEST_CODE = 100;

                int APIVersion = android.os.Build.VERSION.SDK_INT;


                if (APIVersion >= android.os.Build.VERSION_CODES.M) {
                    if(hasPermissions()){
                        Intent intent = new Intent(HomeActivity.this, RecordActivity.class );
                        startActivity(intent);
                    }
                    else{
                        String[] reqPermissionArray = new String[permissions.size()];
                        reqPermissionArray = permissions.toArray(reqPermissionArray);
                        ActivityCompat.requestPermissions(HomeActivity.this, reqPermissionArray, MY_PERMISSIONS_REQUEST_MULTI);

                    }

                }
            }
        });

        btncrop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PopupActivity.class );
                startActivity(intent);
            }
        });

        btnsetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean hasPermissions(){
        boolean flag=true;
        permissions = new ArrayList<String>();
        if (!checkCAMERAPermission()) {
            permissions.add(CAMERA);
            flag = false;
        }
        if(!checkAudioPermission()) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
            flag = false;
        }
        if(!checkReadPermission()) {
            permissions.add(READ_EXTERNAL_STORAGE);
            flag=false;
        }
        if(!checkWritePermission()) {
            permissions.add(WRITE_EXTERNAL_STORAGE);
            flag = false;
        }
        return flag;

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
            //finish();
            moveTaskToBack(true);
            finishAndRemoveTask();
            toast.cancel();

        }
    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

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
                videopath = uri2path.getPath(hcontext, uri2);
            }

        }
    }
    private boolean checkCAMERAPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED;

    }
    private boolean checkAudioPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED;

    }
    private boolean checkReadPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;

    }
    private boolean checkWritePermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {      // 권한 요청을 여러가지 했을 경우를 대비해 switch문으로 묶어 관리한다.
            case MT_PERMISSION_REQUEST_CODE:    //권한 요청시 전달했던 '권한 요청'에 대한 식별 코드
                if (grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);  //권한을 허가받았는지 boolean값으로 저장한다.
                    if (cameraAccepted) {

                      ///  Intent intent = new Intent(HomeActivity.this, RecordActivity.class );
                        //startActivity(intent);
                        //camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);  //카메라를 open() 할 수 있다.


                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {  //이 함수는 권한 요청을 실행한 적이 있고, 사용자가 이를 거절했을 때 true를 리턴한다.


                                showMessagePermission("권한 허가를 요청합니다~ 문구",   //표.2 로 구현


                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, MT_PERMISSION_REQUEST_CODE);  //1)에서 요청했던 함수와 동일
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }

                break;
            case AUDIO_PERMISSION:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);  //권한을 허가받았는지 boolean값으로 저장한다.
                    if (cameraAccepted) {

                       // Intent intent = new Intent(HomeActivity.this, RecordActivity.class );
                       // startActivity(intent);
                        //camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);  //카메라를 open() 할 수 있다.


                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {  //이 함수는 권한 요청을 실행한 적이 있고, 사용자가 이를 거절했을 때 true를 리턴한다.


                                showMessagePermission("권한 허가를 요청합니다~ 문구",   //표.2 로 구현


                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{RECORD_AUDIO}, AUDIO_PERMISSION);  //1)에서 요청했던 함수와 동일
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
            case READ_PERMISSION:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);  //권한을 허가받았는지 boolean값으로 저장한다.
                    if (cameraAccepted) {

                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {  //이 함수는 권한 요청을 실행한 적이 있고, 사용자가 이를 거절했을 때 true를 리턴한다.


                                showMessagePermission("권한 허가를 요청합니다~ 문구",   //표.2 로 구현


                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, READ_PERMISSION);  //1)에서 요청했던 함수와 동일
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
            case WRITE_PERMISSION:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);  //권한을 허가받았는지 boolean값으로 저장한다.
                    if (cameraAccepted) {

                       //Intent intent = new Intent(HomeActivity.this, RecordActivity.class );
                       // startActivity(intent);
                        //camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);  //카메라를 open() 할 수 있다.


                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {  //이 함수는 권한 요청을 실행한 적이 있고, 사용자가 이를 거절했을 때 true를 리턴한다.


                                showMessagePermission("권한 허가를 요청합니다~ 문구",   //표.2 로 구현


                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);  //1)에서 요청했던 함수와 동일
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;

            case MY_PERMISSIONS_REQUEST_MULTI:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);  //권한을 허가받았는지 boolean값으로 저장한다.
                    if (cameraAccepted) {

                        Intent intent = new Intent(HomeActivity.this, RecordActivity.class);
                        startActivity(intent);
                        //camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);  //카메라를 open() 할 수 있다.


                    }
                }


        }


        //AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
        // Toast.makeText(this, "requestCode : "+requestCode+"  permissions : "+permissions+"  grantResults :"+grantResults, Toast.LENGTH_SHORT).show();
    }

    private void showMessagePermission(String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder

                .setMessage(message)

                .setPositiveButton("허용", okListener)

                .setNegativeButton("거부", null)

                .create()

                .show();

    }

}