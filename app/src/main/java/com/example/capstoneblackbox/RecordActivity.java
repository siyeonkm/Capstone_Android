package com.example.capstoneblackbox;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordActivity extends AppCompatActivity implements AutoPermissionsListener{
    //Camera camera;
    MediaRecorder recorder=null;
    String filename;
    SurfaceHolder holder;
    Button record;
    private boolean recording = false;
    long mNow;
    Date mDate;
  //  ImageView gallery;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener impactLis;
    static ArrayList<Long> arr = new ArrayList<>();
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");

    private static long startTime;
    public static DBOpenHelper mDBOpenHelper;
    private static String videoName;

    private long backKeyPressedTime = 0;
    private Toast toast;
    static int impact=0;
    ImageButton gallery;
    static String duration="";
    long second;
    String format_time;
    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    Bitmap bitmap;
    RecordPreview preview;
   // TextView speed;
    TextView timeduration;
    Calendar cal;
    String tempTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record);

        //SurfaceView surface = new SurfaceView(this);
        //holder = surface.getHolder();
        //holder.addCallback(this);
       // holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //camera.open();
/*
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        recorder.setVideoSize(320,240);
        recorder.setVideoFrameRate(15);
        // recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));


 */

        timeduration = findViewById(R.id.timeduration);
        cal = Calendar.getInstance();


        preview = new RecordPreview(this);

        ConstraintLayout layout = findViewById(R.id.container);
        layout.addView(preview);

        gallery =findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //동영상 갤러리로 가서 보여주기
                Intent intent = new Intent(RecordActivity.this, LoadingActivity.class );
                startActivity(intent);
            }
        });

       // speed = findViewById(R.id.speed);

/*
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        format_time = mFormat.format(mDate);

 */


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        impactLis = new ImpactListener();

        record = findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!recording){
                    record.setText("녹화 중지");
                    recording = true;
                    cal.set(Calendar.HOUR_OF_DAY,0);
                    cal.set(Calendar.MINUTE,0);
                    cal.set(Calendar.SECOND,0);

                    tempTime = format2.format(cal.getTime());
                    timeduration.setText(tempTime);
                    timeduration.setVisibility(TextView.VISIBLE);

                    sensorManager.registerListener(impactLis, sensor, SensorManager.SENSOR_DELAY_UI);

                    startTime = System.currentTimeMillis();

                    mDate = new Date(startTime);
                    format_time = mFormat.format(mDate);

                    filename = sdcard + File.separator + "magicBox_"+ format_time + ".mp4";
                    videoName = "magicBox_"+ format_time;
                    //recorder.setOutputFile(videoName);

                    //startRecording();
                    //preview.setFilePath(videoName);
                    preview.start(videoName);

                    (new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            while (!Thread.interrupted())
                                try
                                {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() // start actions in UI thread
                                    {

                                        @Override
                                        public void run()
                                        {
                                            timeduration.setText(getCurrentTime());
                                        }
                                    });
                                }
                                catch (InterruptedException e)
                                {
                                    // ooops
                                }
                        }
                    })).start();
                }
                else{
                    record.setText("녹화 시작");
                    recording = false;
                    sensorManager.unregisterListener(impactLis);
                    long curDateTime = System.currentTimeMillis();
                    second = (curDateTime - startTime) / 1000;
                    long minute = second/60;
                    second%=60;
                    long hour;
                    duration="";
                    if(minute >= 60) {
                        hour = minute / 60;
                        minute%=60;
                        duration = hour+":";
                    }

                    if(minute < 10)
                        duration += "0"+minute+":";
                    else
                        duration += minute+":";
                    if(second < 10)
                        duration+="0"+second;
                    else
                        duration+=second;

                    stopRecording();
                    timeduration.setVisibility(TextView.GONE);
                    Toast.makeText(getApplicationContext(),"저장이 완료됐습니다", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mDBOpenHelper = new DBOpenHelper(this);
        mDBOpenHelper.open();

        Cursor iCursor = mDBOpenHelper.selectColumns();
        iCursor.moveToFirst();

        if(iCursor.moveToNext()){
            String name = iCursor.getString(iCursor.getColumnIndex(DataBases.CreateDB.VideoName));
            String tmpVideoPath = sdcard + File.separator + name + ".mp4";
            Bitmap thumbnail = null;

            try {
                // 썸네일 추출후 리사이즈해서 다시 비트맵 생성
                bitmap = ThumbnailUtils.createVideoThumbnail(tmpVideoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 190,160);

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            gallery.setImageBitmap(thumbnail);
        }


        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    public String getCurrentTime(){
        // 1초 더하기
        cal.add(Calendar.SECOND, 1);
        tempTime = format2.format(cal.getTime());

        return tempTime;
    }

    public static void recordImpact(){
        mDBOpenHelper.insertColumn(videoName, duration,impact);
        impact = 0;
    }

    public void startRecording() {
        if (recorder == null) {
            recorder = new MediaRecorder();
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        //recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        recorder.setOutputFile(filename);

        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();

            recorder.release();
            recorder = null;
        }
    }

    public void stopRecording() {
        preview.stop();

        ContentValues values = new ContentValues(10);

        values.put(MediaStore.MediaColumns.TITLE, "RecordedVideo");
        values.put(MediaStore.Audio.Media.ALBUM, "Video Album");
        values.put(MediaStore.Audio.Media.ARTIST, "Mike");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Video");
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Audio.Media.DATA, filename);

        Uri videoUri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        if (videoUri == null) {
            Log.d("SampleVideoRecorder", "Video insert failed.");
            return;
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, videoUri));
        recordImpact();
        //preview.previewMaintain();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
       // Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        //Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener( impactLis, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(impactLis);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RecordActivity.this, HomeActivity.class);
        startActivity(intent);
    }
/*
    LocationListener locationListener = new LocationListener() {

        // onLocationChanged는 위치 변화가 생기면 자동으로 호출되는 메서드이다.
        @Override
        public void onLocationChanged(Location location) {

            // 현재 속도를 계산할 수 있으면, 현재 속도를 Set 한다.
            if (location.hasSpeed()) {
                float s= location.getSpeed();
                s*=3.6;
                s%=1;
                String temp = s+"";
                speed.setText(temp);
            }
        }



        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };

 */


/*
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if(camera != null){
            Camera.Parameters params = camera.getParameters();
            camera.setParameters(params);
            try{
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
        String filename = sdcard + File.separator + "magicBox_"+ "videotest1" + ".mp4";
        recorder.setOutputFile(filename);
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        }
        catch (Exception e) {
            Log.d("error", "에러 발생");
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

 */

    /*
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        filename = sdcard + File.separator + "magicBox_"+ "videotest1" + ".mp4";
        recorder.setOutputFile(filename);
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        }
        catch (Exception e) {
            Log.d("error", "에러 발생");
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }



     */
    

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

