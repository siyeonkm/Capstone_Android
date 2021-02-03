package com.example.capstoneblackbox;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class RecordScreenActivity extends Activity {
    private static final String TAG = "DATARECORDER";
    private static final int PERMISSION_CODE = 1;
    private MediaProjectionManager mProjectionManager;
    boolean start = false;

    static VideoView videoScreen;
    String videoPath;
    String recordPath;
    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    ImageButton recordstart;
    private String videoFile ;

    static String videoName;
    //String videoStartTime;
    static String tempTime;
    static Calendar cal;
    static TextView timeText;

    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_MediaProjection = 101;

    private MediaProjection mediaProjection;
    static SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static TimerTask second;
    private TextView timer_text;
    private static final Handler handler = new Handler();
    static int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record_screen);

        videoScreen = findViewById(R.id.videoScreen);
        Intent intent = getIntent();
        videoName = intent.getExtras().getString("videoName");

        //String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        videoPath = sdcard + File.separator +"Download/MagicBox/"+ videoName + ".mp4";
       // videoFile = sdcard + File.separator + "Dp"""Report_" + videoName + ".mp4";

        videoScreen.setVideoPath(videoPath);

        recordstart = findViewById(R.id.recordstart);
        timeText = findViewById(R.id.timeText);

        int [] startTime = new int[6];
        int i=0;
        StringTokenizer st = new StringTokenizer(videoName,"_");
        while(st.hasMoreTokens()){
            String temp = st.nextToken();
            if(temp.equals("magicBox"))
                continue;
            startTime[i++] = Integer.parseInt(temp);
        }

        // Java 시간 더하기
        cal = Calendar.getInstance();
        cal.set(startTime[0],startTime[1]-1,startTime[2],startTime[3],startTime[4],startTime[5]);
        Log.d("time", "month "+startTime[1]+" day "+ startTime[2]);

        //tempTime = dayTime.format(cal.getTime());
        timeText.setText(settingTime());

        final MediaController mc = new MediaController(this);
        //videoScreen.setMediaController(mc);
        videoScreen.requestFocus();

        mProjectionManager = (MediaProjectionManager) getSystemService (Context.MEDIA_PROJECTION_SERVICE);


        recordstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 미디어 프로젝션 요청
                if(!start) {
                    recordstart.setVisibility(ImageButton.GONE);
                    //startMediaProjection();
                    start = true;
                    timeText.setVisibility(TextView.VISIBLE);
                    onToggleScreenShare();
                }
                else{
                    start = false;
                    onToggleScreenShare();
                }

            }
        });

        videoScreen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //동영상 재생이 완료된 후 호출되는 메소드
                //Toast.makeText(getApplicationContext(),"재생 완료",Toast.LENGTH_SHORT).show();
                try {
                    start = false;
                    onToggleScreenShare();
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




    }
    public static void testStart() {
        //timeText.setVisibility(TextView.VISIBLE);
        count = 0;
        second = new TimerTask() {
            @Override
            public void run() {
                Log.i("Test", "Timer start");
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }
    public static void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                timeText.setText(getCurrentTime());
            }
        };
        handler.post(updater);
    }
    // 현재 시간을 반환
    public static String getCurrentTime(){

        // 1초 더하기
        cal.add(Calendar.SECOND, 1);
        tempTime = dayTime.format(cal.getTime());

        return tempTime;
    }
    public String settingTime(){

        // 1초 더하기
        cal.add(Calendar.SECOND, -1);
        tempTime = dayTime.format(cal.getTime());

        return tempTime;
    }
    public static void initializeText(){
        timeText.setVisibility(TextView.VISIBLE);
       // timeText.setText(tempTime);

    }
    public void setTimeText(){
        //timeText.setVisibility(TextView.VISIBLE);
        //videoScreen.start();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PERMISSION_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode == RESULT_OK) {
            startRecordingService(resultCode, data);
        }else{
            Toast.makeText(this, "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            //mToggleButton.setChecked(false);
            return;
        }
    }

    public void onToggleScreenShare() {
        if ( start ) {
            // ask for permission to capture screen and act on result after
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);
            Log.v(TAG, "onToggleScreenShare");
        } else {
            Log.v(TAG, "onToggleScreenShare: Recording Stopped");
            stopRecordingService();
        }
    }

    private void startRecordingService(int resultCode, Intent data){
        Intent intent = ScreenCaptureService.newIntent(this, resultCode, data);
        startService(intent);
    }

    private void stopRecordingService(){
        Intent intent = new Intent(this, ScreenCaptureService.class);
        stopService(intent);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


}