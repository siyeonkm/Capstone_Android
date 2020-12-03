package com.example.capstoneblackbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class RecordActivity extends AppCompatActivity {
   /* private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button btn_record;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        
    }
/*
        TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("녹화를 위하여 권한을 허용해주세요.")
                .setDeniedMessage("권한이 거부되었습니다. 설정 > 권한에서 허용해주세요.")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .check();


    }

    PermissionListener permission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(RecordActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();

            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(RecordActivity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(RecordActivity.this, "권한 거부", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

 */
}