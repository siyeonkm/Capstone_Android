package com.example.capstoneblackbox;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordPreview extends SurfaceView implements SurfaceHolder.Callback {
    MediaRecorder recorder = null;
    SurfaceHolder surfaceHolder;
    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    boolean recording = false;
    Camera camera;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
    long mNow;
    Date mDate;
    private static long startTime;
    String format_time;
    String filename;
    String videoName;

    public RecordPreview(Context context){
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);


        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera = Camera.open();

       prepareRecording();

    }

    public void prepareRecording(){
        if (recorder == null) {
            recorder = new MediaRecorder();

        //camera = Camera.open();
        //recorder.setCamera(camera);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        /*
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        recorder.setVideoSize(320,240);
        recorder.setVideoFrameRate(15);

         */
        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        }
    }
    public void previewMaintain(){
        prepareRecording();
        recorder.setPreviewDisplay(surfaceHolder.getSurface());

        try {
            recorder.prepare();
        }
        catch (Exception e) {
            Log.d("error", "에러 발생");
            e.printStackTrace();
        }
    }

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
        //prepareRecording();
        //filename = sdcard + File.separator + "magicBox_"+ "videotest1" + ".mp4";
        //recorder.setOutputFile(filename);
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
        //refreshCamera(camera);
    }

    public void refreshCamera(Camera camera) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            this.camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        setCamera(camera);
        try {
            this.camera.setPreviewDisplay(surfaceHolder);
            this.camera.startPreview();
            //previewMaintain();
        } catch (Exception e) {
        }
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        //camera.stopPreview();
        //camera.release();
    }

    public MediaRecorder getRecorder(){
        return recorder;
    }

    public void setRecorder(MediaRecorder recorder) {
        this.recorder = recorder;
    }

    public void start(String videoName){
        filename = sdcard + File.separator + videoName + ".mp4";
        //camera.unlock();
        prepareRecording();
        recorder.setOutputFile(filename);
        recorder.setPreviewDisplay(surfaceHolder.getSurface());
        try {
            recorder.prepare();
        }
        catch (Exception e) {
            Log.d("error", "에러 발생");
            e.printStackTrace();
        }
        recorder.start();
    }

    public void stop(){
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
    //camera.lock();


    }

}
