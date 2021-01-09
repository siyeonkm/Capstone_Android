package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivityMainBinding;

import io.realm.Realm;
import io.realm.RealmConfiguration;


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

    public final ConnectServer connectServerPost = new ConnectServer();

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
                    connectServerPost.requestPost("http://4982a1961666.ngrok.io/login", id, pw);

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

}