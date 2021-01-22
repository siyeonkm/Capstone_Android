package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivitySettingBinding;

import static com.example.capstoneblackbox.MainActivity.mcontext;

//홈화면에서 맨위 왼쪽 바3개(설정) 클릭시 열리는 설정창 >> 마이페이지 정도의 용도로 사용하려고함
//본인 사진, 아이디변경, 사진변경, 내 비디오 보기, 로그아웃 버튼 만들어놓음
//현재까지 제일 필요한 내 비디오 보기만 만들어놓은 상태
public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    public static Context stcontext;

    ImageButton setting;
    Button logout;
    Button myVid;

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

        myVid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                UriToPath u2path = new UriToPath();
                String real_path = "/storage/emulated/0/Download/";


                //데이터 받아와지는지만 확인하려고 이거밖에 안 써놓음
                for(int i=1; i<=3; i++) {

                    ((MainActivity) mcontext).connectServerPost
                            .requestVideoGet("http://b049b8cfa4d4.ngrok.io/input/full", real_path, i);
                }

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