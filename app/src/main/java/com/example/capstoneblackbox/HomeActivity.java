package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivityHomeBinding;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    public static Context hcontext;
    private long backKeyPressedTime = 0;
    private Toast toast;

    ImageButton btnvideo;
    ImageButton btncrop;

    String date = "2021-01-01 12:30:01";
    String size = "1000";
    String path = "videos";
    String usernum = "1";
    String videopath;

    int timeout = 100000;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        hcontext = this;

        btnvideo = binding.videoButton;
        btncrop = binding.cropButton;

        ConnectServer connectServerPost = new ConnectServer();

        videopath = "android.resource://" + getPackageName() +"/raw/test2";

        btnvideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               ConnectServer connectServerPost = new ConnectServer();
               connectServerPost.requestPost("http://2c5a42a8b05b.ngrok.io/api/full", videopath, usernum, path, size, date);

                //기본 카메라 연결
                /*Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                   startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }*/

                //Intent intent = new Intent(HomeActivity.this, RecordActivity.class );
                //startActivity(intent);
            }
        });

        btncrop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PopupActivity.class );
                startActivity(intent);
            }
        });
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

    class ConnectServer{
        //Client 생성
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        public void requestPost(String url, String video, String usernum, String path, String size, String date){

            //Request Body에 서버에 보낼 데이터 작
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("full_video", "test.mp4", RequestBody.create(MultipartBody.FORM, new File(video)))
                    .addFormDataPart("date", date)
                    .addFormDataPart("size", size)
                    .addFormDataPart("storage_path", path)
                    .addFormDataPart("user_id", usernum).build();

            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(requestBody).build();

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    Log.d("aaaa", "Response Body is " + response.body().string());
                }
            });
        }





    }
}