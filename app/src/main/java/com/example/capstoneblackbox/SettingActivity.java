package com.example.capstoneblackbox;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneblackbox.databinding.ActivitySettingBinding;

import static com.example.capstoneblackbox.MainActivity.mcontext;

//홈화면에서 맨위 왼쪽 바3개(설정) 클릭시 열리는 설정창 >> 마이페이지 정도의 용도로 사용하려고함
//본인 사진, 아이디변경, 사진변경, 내 비디오 보기, 로그아웃 버튼 만들어놓음
//현재까지 제일 필요한 내 비디오 보기만 만들어놓은 상태
public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    public static Context stcontext;

    Button chgId;
    Button logout;
    Button myVid;

    public int succeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        stcontext = this;

        chgId = binding.buttonChgId;
        myVid = binding.buttonMyVid;
        logout = binding.buttonLogout;

        myVid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //데이터 받아와지는지만 확인하려고 이거밖에 안 써놓음
                ((MainActivity) mcontext).connectServerPost
                        .requestGet("http://48f7a7e01ee7.ngrok.io/api/full");

            }
        });

    }
}