package com.example.capstoneblackbox;

import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

//서버에 연결해주는 각종 함수들 가짐
//딱한번 cookiejar생성, 딱하나의 client를 생성함.
//post 2종류(로그인용, 비디오업로드용), get 한 종류(비디오다운용) 만들어져있음
public class ConnectServer extends Thread{

    public final MyCookieJar myCookieJar = new MyCookieJar();
    public final OkHttpClient client = new OkHttpClient.Builder().cookieJar(myCookieJar).build();

    public int exist = 0;

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
        notify();
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
                synchronized (this) {
                    if(res.equals("ok")) {
                        exist = 1;

                    }
                    else{
                        exist = 0;
                    }
                    notify();
                }
            }
        });
    }

    public void requestGet(String url, final List userArr, int fin) {
        final String path = MediaStore.Video.Media.EXTERNAL_CONTENT_URI + "/magicbox";

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
                ResponseBody rBody = response.body();
                String htmlstr = rBody.string();
                String result1;
                String result2[];
                String result3[] = new String[50];
                int j = 0; int i = 0;

                //Log.d("aaaa", "Response Body is " + htmlstr);
                result1 = htmlstr.substring(htmlstr.indexOf("<td>")+4, htmlstr.lastIndexOf("</td>"));
                result2 = result1.split("<td>|</td>");


                for(i = 0, j=0; i < result2.length; i++) {
                    if(result2[i].contains("<!--") || result2[i].contains("-->") || result2[i].contains("\n")
                    || result2[i].contains("수정") || result2[i].contains("삭제")) {

                    }
                    else {
                        result3[j] = result2[i];
                        j++;
                    }
                }

                for(i=0; i<j; i++) {
                    if(result3[i].contains("</a>")) {
                        result3[i] = result3[i].substring(result3[i].indexOf(">")+1, result3[i].lastIndexOf("</a>"));
                    }
                }

                for(i = 1; i< j+1; i++) {
                    if(i%6 == 1) {
                        UserInfo userInfo = new UserInfo(result3[i-1], result3[i], result3[i+1], result3[i+2], result3[i+3], result3[i+4]);
                        userArr.add(userInfo);
                    }
                    System.out.println(i + ": " + result3[i-1]);
                }
            }
        });
        notify();
    }



}
