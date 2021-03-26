package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivitySettingBinding;

//홈화면에서 맨위 왼쪽 바3개(설정) 클릭시 열리는 설정창 >> 마이페이지 정도의 용도로 사용하려고함
//본인 사진, 아이디변경, 사진변경, 내 비디오 보기, 로그아웃 버튼 만들어놓음
//현재까지 제일 필요한 내 비디오 보기만 만들어놓은 상태
public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    public static Context stcontext;

    ImageButton setting;
    Button logout;
    Button myVid;

    TextView helloUser;
    TextView name;
    public String vidpath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        stcontext = this;

        myVid = binding.buttonMyVid;
        logout = binding.buttonLogout;
        setting = binding.btnSetting;
        helloUser = binding.hello2;
        name = binding.txtUserId;

        helloUser.setText(((MainActivity)MainActivity.mcontext).id + "님!");
        name.setText(((MainActivity)MainActivity.mcontext).id+"님");

        myVid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, LoadingActivity.class );
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


}