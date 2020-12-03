package com.example.capstoneblackbox;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.capstoneblackbox.databinding.ActivityMainBinding;

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

    int idErr; int pwErr;

    private String pwValidation = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d].{8,15}.$";
    private String idValidtion = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";

    //oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mcontext = this;

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
                else if(idErr == 1 || pwErr == 1) {
                    Toast.makeText(mcontext,"형식에 맞춰서 작성해주십시오",Toast.LENGTH_SHORT).show();
                }
                else {
                    //TODO: id와 pw를 db에서 찾아서 >> 없으면 goHomeActivity
                    goHomeActivity();
                    //TODO: >> 있으면 토스트 발생 & 안넘어감
                    //Toast.makeText(mcontext, "입력하신 아이디/패스워드가 이미 존재합니다", Toast.LENGTH_LONG).show();
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
                if(!pw.matches(pwValidation) || pw.length() == 0 ) {
                    pwerr.setVisibility(View.VISIBLE);
                    pwErr = 1;
                }
                else {
                    pwerr.setVisibility(View.GONE);
                    pwErr = 0;
                }
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