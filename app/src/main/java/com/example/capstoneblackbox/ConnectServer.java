package com.example.capstoneblackbox;

import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectServer {

    public final OkHttpClient client = new OkHttpClient();

    public void requestPost(String url, String video, String path, String size, String date, int user_id) {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("full_video", "filepathReal.mp4", RequestBody.create(MediaType.parse("video/mp4"), new File(video)))
                .addFormDataPart("date", date)
                .addFormDataPart("size", size)
                .addFormDataPart("storage_path", path)
                .addFormDataPart("user_id",Integer.toString(user_id)).build();

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        call.enqueue(new Callback() {
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

    public void requestPost(String url, final String id, final String password){

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder().add("email", id)
                .add("password", password).build();

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
                String res = response.body().string();
                Log.d("aaaa", "Response Body is " + res);
                if(res.equals("ok")) {
                    ((MainActivity)MainActivity.mcontext).setExist(1);

                }
                else{
                    ((MainActivity)MainActivity.mcontext).setExist(0);
                }
            }
        });
    }

    public void requestGet(String url) {
        String path = MediaStore.Video.Media.EXTERNAL_CONTENT_URI + "/magicbox";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            //비동기 처리를 위해 Callback 구현
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("error + Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Response Body is " + response.body().string());
            }
        });


    }


}
