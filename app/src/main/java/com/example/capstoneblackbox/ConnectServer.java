package com.example.capstoneblackbox;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static io.realm.Realm.getApplicationContext;

//서버에 연결해주는 각종 함수들 가짐
//딱한번 cookiejar생성, 딱하나의 client를 생성함.
//post 2종류(로그인용, 비디오업로드용), get 한 종류(비디오다운용) 만들어져있음
public class ConnectServer {

    public final MyCookieJar myCookieJar = new MyCookieJar();
    public final OkHttpClient client = new OkHttpClient.Builder().cookieJar(myCookieJar)
            .connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.MINUTES).build();

    String user_id="";

    public static boolean t = true;
    public int video=1;
    public int vidcnt=0;

    public void requestPost(String url, String video, String path, String size, String date, int user_id) {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("full_video", "abnorm.mp4", RequestBody.create(MediaType.parse("video/mp4"), new File(video)))
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
                Log.d("ERROR", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String vidname = response.body().string();
                Log.d("MESSAGE", "Response Body is " + vidname);
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 사용하고자 하는 코드
                        Toast.makeText(MainActivity.mcontext, vidname + " 업로드 완료!", Toast.LENGTH_LONG).show();
                    }
                }, 0);
                ((AbnormUploadActivity)AbnormUploadActivity.abupcontext).fromAbUptoHomeActivity();
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
                Toast.makeText(MainActivity.mcontext, "서버 오류", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("aaaa", "Response Body is " + res);
                if(res.length()<5) {
                    user_id = "0" + res.substring(0, res.length()-1);
                    ((MainActivity)MainActivity.mcontext).goHomeActivity();

                }
                else{
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 사용하고자 하는 코드
                            Toast.makeText(MainActivity.mcontext, "없는 아이디입니다", Toast.LENGTH_LONG).show();
                        }
                    }, 0);

                }
            }
        });
    }

    //같은 아이디인지 확인하는거
    public void requestSameId(String url, final String id, final String password){

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
                Toast.makeText(MainActivity.mcontext, "서버 오류", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("aaaa", "Response Body is " + res);
                if(res.equals("ok")) {
                    ((SigninActivity)SigninActivity.scontext).goMainActivity();

                }
                else{
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 사용하고자 하는 코드
                            Toast.makeText(MainActivity.mcontext, "이미 존재하는 아이디입니다", Toast.LENGTH_LONG).show();
                        }
                    }, 0);

                }
            }
        });
    }

    public void requestVideoCnt(String svurl){
        Request request = new Request.Builder()
                .url(svurl)
                .build();

        client.newCall(request).enqueue(new Callback() {

            //비동기 처리를 위해 Callback 구현
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ERROR", "error + Connect Server Error is " + e.toString());
                t = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers responseHeaders = response.headers();
                String vidcnt_s = response.body().string();
                vidcnt = Integer.parseInt(vidcnt_s);
                Log.d("MESSAGE", "동영상 개수: " + vidcnt_s);
                ((Popup2Activity)Popup2Activity.p2context).popup_to_ab();
            }
        });
    }

    public void requestVideoGet(String svurl)
    {
        for(video=1; video<11; video++) {
            String vid_name = "edited"+ user_id + "0"+ String.valueOf(video) + "01" + ".mp4";
            String vid_url = svurl + "/" +vid_name;

            Request request = new Request.Builder()
                    .url(vid_url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                private File mediaFile;

                //비동기 처리를 위해 Callback 구현
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("ERROR", "error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream inputStream = null;
                    inputStream = response.body().byteStream();
                    String err = new String();

                    try {
                        byte[] buff = new byte[1024 * 4];
                        long downloaded = 0;
                        long target = response.body().contentLength();

                        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath().toString() + "/MagicBoxAbnormal";
                        File dirFile = new File(dir);

                        //매직박스용 외부저장소 폴더 생성
                        if(!dirFile.exists()) {
                            dirFile.mkdirs();
                        }

                        mediaFile = new File(dir + "/" + vid_name);

                        if (this.mediaFile.exists()) {
                            this.mediaFile.delete();
                        }


                        OutputStream output = new FileOutputStream(mediaFile);

                        while (true) {
                            int readed = inputStream.read(buff);

                            if (readed == -1) {
                                break;
                            }
                            output.write(buff, 0, readed);
                            //write buff
                            downloaded += readed;
                        }
                        output.flush();
                        output.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally{
                        if(inputStream != null){
                            inputStream.close();
                        }
                    }
                    MediaScanner mediaScanner = new MediaScanner(getApplicationContext(), mediaFile);
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 사용하고자 하는 코드
                            Toast.makeText(MainActivity.mcontext, vid_name + "영상 다운로드 완료! 갤러리에서 확인하실 수 있습니다", Toast.LENGTH_SHORT).show();
                        }
                    }, 0);
                }
            });
        }
    }
}
