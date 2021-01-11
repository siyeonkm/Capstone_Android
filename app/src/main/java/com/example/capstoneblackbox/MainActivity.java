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
               else {

                   //TODO: >> 있으면 넘어감
                    connectServerPost.start();
                    connectServerPost.requestPost("http://93a969d683bd.ngrok.io/login", id, pw);

                   synchronized(connectServerPost) {
                       try {
                           // b.wait()메소드를 호출.
                           // 메인쓰레드는 정지
                           // ThreadB가 5번 값을 더한 후 notify를 호출하게 되면 wait에서 깨어남
                           System.out.println("b가 완료될때까지 기다립니다.");
                           connectServerPost.wait();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
                   if(connectServerPost.exist == 1) {
                        goHomeActivity();
                   }
                   //TODO: 토스트 발생 & 안넘어감
                   else if (connectServerPost.exist == 0) {
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

}