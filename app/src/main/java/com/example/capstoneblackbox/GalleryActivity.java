package com.example.capstoneblackbox;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener{

    private ArrayList<Video> list;
    MyAdapter adapter;
    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    String videoName;
    Cursor iCursor;
    DBOpenHelper mDBOpenHelper;
    String videoPath;
    Bitmap thumbnail;
    boolean impact;
    int button;
    String duration;
    Context context;
    RecyclerView recyclerView;
    //Bitmap bitmap;
    private long backKeyPressedTime = 0;
    TextView novideo;
    ImageView trash;
    CheckBox checkBox;
    ImageView photolist_deletecancel;
    ImageView photolist_deleteok;

    //int visibility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);

        mDBOpenHelper = new DBOpenHelper(this);
        try {
            mDBOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        context = this;
        this.InitializeData();

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        //LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager); // LayoutManager 등록
        adapter = new MyAdapter(context,list,this);

        recyclerView.setAdapter(adapter);  // Adapter 등록

        checkBox = findViewById(R.id.checkBox);

        photolist_deletecancel = findViewById(R.id.photolist_deletecancel);
        photolist_deleteok = findViewById(R.id.photolist_deleteok);

        trash = findViewById(R.id.trash);

        trash.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(list.size()!=0)
                    buttonClicked(1);
            }
        });
        TextView novideo = findViewById(R.id.novideo);
        if(list.size()==0){
            recyclerView.setVisibility(View.GONE);
            novideo.setVisibility(View.VISIBLE);
        }

        photolist_deletecancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                buttonClicked(0);
            }
        });

        photolist_deleteok.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                delete();
            }
        });


        //novideo = findViewById(R.id.novideo);

        /*
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Video video) {
                Intent intent = new Intent(GalleryActivity.this,VideoPlayActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

         */

    }

    public void delete(){
        int[] positions = new int[adapter.getItemCount()];
        // 처음부터 체우기 위한 변수
        int index = 0;

        for(int i = 0; i < adapter.getCheckBoxSize(); i++){
            if(adapter.getCheckBoxItem(i))
                // 체크가 되어 있다면 배열에 처음부터 저장하고 index값을 1씩 증가 시킨다.
                positions[index++] = i;
        }


        // 실제 배열에 들어있는 데이터의 갯수와 같기 때문에 인덱스로 쓰기 위해서는 1만큼 빼서 사용한다.
        // 이때 앞에 있는 아이템부터 삭제를 시작한다면 ArrayList특성상 하나씩 다시 밀리기 때문에 지우고자 하는 아이템의
        // 위치가 틀려진다.
       for ( int i = index - 1; i >= 0; --i ) {
            // 어뎁터에서 체크된 위치의 아이템을 하나씩 가져온다.
            //ListData temp = (ListData)adapter.getItem(positions[i]);
            // 쿼리문에 사용할 데이터를 지우기전 미리 저장
            Video temp = adapter.getVideo(positions[i]);
            String tempName = temp.getVideoName();
            // 체크된 아이템 삭제
            adapter.removeVideoList(positions[i]);
            adapter.removeCheckBoxList(positions[i]);
            // 쿼리문 작성후 쿼리 전송
            //query = String.format("DELETE FROM list WHERE imagePath='%s' and name='%s' and phone='%s';", datas[0],datas[1], datas[2]);
            //db.execSQL(query);

           String tempFilePath = sdcard+ File.separator +tempName+".mp4";
           File file = new File(tempFilePath);
           if( file.exists() ) {
               if(file.delete()){
                   Log.d("delete","파일삭제 성공");
                   mDBOpenHelper.deleteColumn(tempName);
               }
               else{
                   Log.d("delete","파일삭제 실패");
               }
           }
           else{
               Log.d("error","파일이 존재하지 않습니다");
           }



        }
        // 삭제를 하였으니 삭제버튼을 다시 감춘다.
        buttonClicked(0);
        if(list.size()==0){
            recyclerView.setVisibility(View.GONE);
            novideo.setVisibility(View.VISIBLE);
        }
    }

    public void buttonClicked(int n){
        adapter.updateCheckbox(n);
        adapter.initializeCheckBox();
        adapter.setCheckBoxFlag(n);
        //adapter.releaseCheckBox();
        adapter.notifyDataSetChanged();

        if(n==1){//휴지통 누른 경우
            adapter.createCheckBoxList();
            trash.setVisibility(View.GONE);
            photolist_deletecancel.setVisibility(View.VISIBLE);
            photolist_deleteok.setVisibility(View.VISIBLE);
            //radiobt.visibility = View.VISIBLE
        }
        else if(n==0){
            photolist_deletecancel.setVisibility(View.GONE);
            photolist_deleteok.setVisibility(View.GONE);
            trash.setVisibility(View.VISIBLE);
        }
    }

    public void InitializeData(){
        list = new ArrayList<>();

        iCursor = mDBOpenHelper.selectColumns();
        iCursor.moveToFirst();
        /*
        if(!iCursor.moveToNext()){
            novideo.setVisibility(View.VISIBLE);
        }

         */

        while(iCursor.moveToNext()){
            videoName = iCursor.getString(iCursor.getColumnIndex(DataBases.CreateDB.VideoName));
            duration = iCursor.getString(iCursor.getColumnIndex(DataBases.CreateDB.Duration));
            impact = iCursor.getInt(iCursor.getColumnIndex(DataBases.CreateDB.Impact))>0;
            videoPath = sdcard + File.separator + videoName + ".mp4";
            thumbnail = null;

            File files = new File(videoPath);
            //파일 유무를 확인
            if(!files.exists()) {
                //파일이 없을시
                Log.d("확인","비디오 없음"+videoName);
                mDBOpenHelper.deleteColumn(videoName);
                continue;
            }

            try {
                /*
                Glide.with(this)
                        .asBitmap().load(videoPath)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .listener(new RequestListener<Bitmap>() {
                                      @Override
                                      public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                          return false;
                                      }

                                      @Override
                                      public boolean onResourceReady(Bitmap bitmap1, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                          Log.d("TAG", "비트맵변환한거0 => " + bitmap);
                                          bitmap=bitmap1;
                                          return false;
                                      }
                                  }
                        ).submit();

                 */
                // 썸네일 추출후 리사이즈해서 다시 비트맵 생성
               Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
               thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 1000,800);
               // thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 1000,800);
                if(!impact)
                    button = R.drawable.play;
                else
                    button = R.drawable.play_impact;

                list.add(new Video(thumbnail, videoName, button, duration));
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.d("db 읽기","못 가져와 : "+videoName);
            }

            //Log.d("db 읽기","비디오 이름 : "+videoName);
        }
        //videoName = "magicBox_"+ "2021_01_17_12_04_02";
        /*
        if(list.size()==0) {
            recyclerView.setVisibility(View.GONE);
            novideo.setVisibility(View.VISIBLE);

        }

         */

        //list.add(new Video());
    }


    @Override
    public void onItemClick(View view, String videoName) {
//        MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(GalleryActivity.this, VideoPlayActivity.class);
        intent.putExtra("videoName", videoName);
       // intent.putExtra("bitmap", bitmap);
        startActivity(intent);

        //Toast.makeText(this, viewHolder.)
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GalleryActivity.this, RecordActivity.class);
        startActivity(intent);
    }



}