package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static Context mcontext;

    Button btnlogin;
    Button btnsignin;

    EditText txtId;
    EditText txtPw;

    TextView iderr;
    TextView pwerr;

    String id;
    String pw;

    Realm realm;
    public int exist = 3;

    int idErr; int pwErr;

    ConnectServer connectServerPost;

    //private String idValidation = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d].{8,15}.$";
    //private String pwValidation = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";

    //oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mcontext = this;

        Realm.init(mcontext);
        RealmConfiguration config = new RealmConfiguration.Builder().name("userDb.realm").build();
        Realm.setDefaultConfiguration(config);

        btnlogin = binding.buttonLogin;
        txtId = binding.textId;
        txtPw = binding.textPassword;

        btnlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                id = txtId.getText().toString();
                pw = txtPw.getText().toString();

               if(id.compareTo("") == 0 || pw.compareTo("") == 0){
                    Toast.makeText(mcontext, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                }
                /*else if(idErr == 1 || pwErr == 1) {
                    Toast.makeText(mcontext,"형식에 맞춰서 작성해주십시오",Toast.LENGTH_SHORT).show();
                }*/
                else {
                   exist = 3;

                   //TODO: >> 있으면 넘어감
                    connectServerPost = new ConnectServer();
                    connectServerPost.requestPost("http://0e75c4615da7.ngrok.io/login", id, pw);

                    //서버한테 ok응답 받을때까지 기다림
                    while(exist ==3) {
                    }

                    if(exist == 1) {
                        goHomeActivity();
                    }
                    //TODO: 토스트 발생 & 안넘어감
                    else {
                        Toast.makeText(mcontext, "없는 아이디입니다. 다시 입력해주세요", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        iderr = binding.idError;
        pwerr = binding.pwError;

        txtId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                id = txtId.getText().toString();
                iderr.setVisibility(View.GONE);
                idErr = 0;
                /*if(!id.matches(idValidation) || id.length() == 0 ) {
                    iderr.setVisibility(View.VISIBLE);
                    idErr = 1;
                }
                else {
                    iderr.setVisibility(View.GONE);
                    idErr = 0;
                }*/
            }
        });

        txtPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                pw = txtPw.getText().toString();
                pwerr.setVisibility(View.GONE);
                pwErr = 0;
                /*if(!pw.matches(pwValidation) || pw.length() == 0 ) {
                    pwerr.setVisibility(View.VISIBLE);
                    pwErr = 1;
                }
                else {
                    pwerr.setVisibility(View.GONE);
                    pwErr = 0;
                }*/
            }
        });

        btnsignin = binding.buttonSignin;
        btnsignin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goSigninActivity();
            }
        });
    }

    //oncreate 바깥에 있는 함수들
    public void goHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void goSigninActivity() {
        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
        startActivity(intent);
    }

    public void setExist(int e) {
        this.exist = e;
    }

    public class ConnectServer {

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();


        public void requestPost(String url, String video, String path, String size, String date) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("full_video", "filepathReal.mp4", RequestBody.create(MediaType.parse("video/mp4"), new File(video)))
                    .addFormDataPart("date", date)
                    .addFormDataPart("size", size)
                    .addFormDataPart("storage_path", path).build();

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

        public void requestGet(final Context context, String url) {
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

}