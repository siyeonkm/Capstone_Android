package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivitySigninBinding;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SigninActivity extends AppCompatActivity {
    ActivitySigninBinding binding;
    public static Context scontext;

    Button btnsignin;

    EditText txtId;
    EditText txtEmail;
    EditText txtPw;

    TextView iderr;
    TextView emailerr;
    TextView pwerr;

    int idErr; int pwErr;

    String id;
    String pw;

    private String pwValidation = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d].{8,15}.$";
    private String idValidtion = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        scontext = this;

        btnsignin = binding.buttonSignin;

        txtId = binding.textId;
        txtPw = binding.editTextTextPassword;


        ConnectServer connectServerPost = new ConnectServer();

        btnsignin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                id = txtId.getText().toString();
                pw = txtPw.getText().toString();

                if(id.compareTo("") == 0 || pw.compareTo("") == 0){
                    Toast.makeText(scontext, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    //TODO: id와 pw를 db에서 찾아서 >> 없으면 goHomeActivity
                    ConnectServer connectServerPost = new ConnectServer();
                    connectServerPost.requestPost("http://44af370b0d8d.ngrok.io/api/user", id, pw);

                    goHomeActivity();
                    //TODO: >> 있으면 토스트 발생 & 안넘어감
                    //Toast.makeText(mcontext, "입력하신 아이디/패스워드가 이미 존재합니다", Toast.LENGTH_LONG).show();
                }
            }
        });

        //밑에 3개 함수는 실시간으로 형식 틀렸는지 알려주는것 (아이디, 비번, 이메일)
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
                if(!id.matches(idValidtion) || id.length() == 0 ) {
                    iderr.setVisibility(View.VISIBLE);
                    idErr = 1;
                }
                else {
                    iderr.setVisibility(View.GONE);
                    idErr = 0;
                }
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
                if(!pw.matches(pwValidation) || id.length() == 0 ) {
                    pwerr.setVisibility(View.VISIBLE);
                    pwErr = 1;
                }
                else {
                    pwerr.setVisibility(View.GONE);
                    pwErr = 0;
                }
            }
        });
    }

    public void goHomeActivity() {
        Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    static class ConnectServer{
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        public void requestPost(String url, String id, String password){

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new FormBody.Builder().add("email", id).add("password", password).build();

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