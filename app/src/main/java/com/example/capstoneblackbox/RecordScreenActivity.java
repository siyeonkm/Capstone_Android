package com.example.capstoneblackbox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pedro.library.AutoPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class RecordScreenActivity extends AppCompatActivity {

    VideoView videoScreen;
    String videoPath;
    String recordPath;
    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    ImageButton recordstart;
    private String videoFile ;

    MediaRecorder screenRecorder;
    MediaProjectionManager mediaProjectionManager;

    String videoName;
    //String videoStartTime;
    String tempTime;
    Calendar cal;
    TextView timeText;

    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_MediaProjection = 101;

    private MediaProjection mediaProjection;
    SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record_screen);

        AutoPermissions.Companion.loadAllPermissions( this,  101);

        videoScreen = findViewById(R.id.videoScreen);
        Intent intent = getIntent();
        videoName = intent.getExtras().getString("videoName");

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        videoPath = sdcard + File.separator + videoName + ".mp4";
        videoFile = sdcard + File.separator + "Report_" + videoName + ".mp4";

        videoScreen.setVideoPath(videoPath);

        recordstart = findViewById(R.id.recordstart);
        timeText = findViewById(R.id.timeText);

        int [] start = new int[6];
        int i=0;
        StringTokenizer st = new StringTokenizer(videoName,"_");
        while(st.hasMoreTokens()){
            String temp = st.nextToken();
            if(temp.equals("magicBox"))
                continue;
            start[i++] = Integer.parseInt(temp);
        }

        // Java 시간 더하기
        cal = Calendar.getInstance();
        cal.set(start[0],start[1]-1,start[2],start[3],start[4],start[5]);
        Log.d("time", "month "+start[1]+" day "+ start[2]);

        tempTime = dayTime.format(cal.getTime());
        timeText.setText(tempTime);

        final MediaController mc = new MediaController(this);
        videoScreen.setMediaController(mc);
        videoScreen.requestFocus();

        //MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        //startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 101);


/*
        videoScreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                recorder.start();
                videoScreen.start();
                Log.d("state","상태 : 시작");
            }
        });

 */

        //videoScreen.start();
        // 퍼미션 확인

/*
        videoScreen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //동영상 재생이 완료된 후 호출되는 메소드
                Toast.makeText(getApplicationContext(),"재생 완료",Toast.LENGTH_SHORT).show();

            }
        });

 */

        checkSelfPermission();

    }


    // 현재 시간을 반환
    public String getCurrentTime(){

        // 1초 더하기
        cal.add(Calendar.SECOND, 1);
        tempTime = dayTime.format(cal.getTime());

        return tempTime;
    }


    @Override
    protected void onDestroy() {
        // 녹화중이면 종료하기
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable final Intent data) {
        // 미디어 프로젝션 응답
        if (requestCode == REQUEST_CODE_MediaProjection && resultCode == RESULT_OK) {
            screenRecorder(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 음성녹음, 저장소 퍼미션
     *
     * @return
     */
    public boolean checkSelfPermission() {
        String temp = "";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.RECORD_AUDIO + " ";
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), REQUEST_CODE_PERMISSIONS);
            return false;
        } else {
            initView();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                int length = permissions.length;
                for (int i = 0; i < length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 퍼미션 동의가 1개라도 부족하면 화면을 초기화 하지 않음
                        return;
                    }
                    initView();
                }
                return;
            }
            default:
                return;
        }
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        recordstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 미디어 프로젝션 요청
                recordstart.setVisibility(ImageButton.GONE);
                startMediaProjection();

            }
        });
    }

    /**
     * 화면녹화
     *
     * @param resultCode
     * @param data
     */
    private void screenRecorder(int resultCode, @Nullable Intent data) {

        screenRecorder = createRecorder();
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);

        MediaProjection.Callback callback = new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                if (screenRecorder != null) {
                    screenRecorder.stop();
                    screenRecorder.reset();
                    screenRecorder.release();
                }
                mediaProjection.unregisterCallback(this);
                mediaProjection = null;
            }
        };
        mediaProjection.registerCallback(callback, null);

        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        mediaProjection.createVirtualDisplay(
                "sample",
                displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                screenRecorder.getSurface(), null, null);

        videoScreen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //동영상 재생이 완료된 후 호출되는 메소드
                //Toast.makeText(getApplicationContext(),"재생 완료",Toast.LENGTH_SHORT).show();
                try {
                    mediaProjection.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timeText.setVisibility(TextView.GONE);

                Toast.makeText(getApplicationContext(), "저장되었습니다",Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setDataAndType(Uri.parse(videoFile), "video/mp4");
                //startActivity(intent);
            }
        });
/*
        Button actionRec = findViewById(R.id.recordstart);
        actionRec.setText("STOP REC");
        actionRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionRec.setText("START REC");
                if (mediaProjection != null) {
                    try {
                        mediaProjection.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(videoFile), "video/mp4");
                    startActivity(intent);
                }
            }
        });

 */


        timeText.setVisibility(TextView.VISIBLE);
        screenRecorder.start();
        videoScreen.start();
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
                                timeText.setText(getCurrentTime());
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

    /**
     * 미디어 프로젝션 요청
     */
    private void startMediaProjection() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE_MediaProjection);
        }
    }

    /**
     * 미디어 레코더
     *
     * @return
     */
    private MediaRecorder createRecorder() {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

        //recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        //mediaRecorder.setOutputFile(filename);

        //DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        //mediaRecorder.setVideoSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
        //mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //mediaRecorder.setVideoEncodingBitRate(512 * 1000);
        //mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mediaRecorder.setOutputFile(videoFile);
        Log.d("filepath", "경로 "+videoFile);
        try {
            mediaRecorder.prepare();
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return mediaRecorder;
    }
    

/*
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable final Intent data) {
        // 미디어 프로젝션 응답
        if (requestCode == 101 && resultCode == RESULT_OK) {
            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

            recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
            recorder.setOutputFile(videoFile);
            //recorder.setPreviewDisplay(holder.getSurface());
            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            recorder.setVideoSize(displayMetrics.widthPixels, displayMetrics.heightPixels);


            try {
                recorder.prepare();

            }
            catch (Exception e) {
                Log.d("error", "상태 prepare 이상");
                e.printStackTrace();
            }
            try {
                recorder.start();
            } catch (IllegalStateException e) {
                Log.d("error", "상태 start 이상");
                e.printStackTrace();
            }
            videoScreen.start();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void createRecorder() {

            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

            recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
            recorder.setOutputFile(videoFile);
        //recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        }
        catch (Exception e) {
            Log.d("error", "상태 prepare error 발생");
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    @Override
    public void onDenied(int i, String[] strings) {
        Toast.makeText(this, "permissions denied : " + strings.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this, "permissions granted : " + strings.length, Toast.LENGTH_LONG). show();
    }

 */


}