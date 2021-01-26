package com.example.capstoneblackbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static io.realm.Realm.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    Context context;
    private ArrayList<Video> list = null;
    public OnItemClickListener mOnItemClickListener = null;
    //public OnItemClickListener mOnMenuClickListener = null;

    private int checkBoxFlag=0;
    private int visibility;
    private ArrayList<Boolean> checkboxList;

    public interface OnItemClickListener extends AdapterView.OnItemClickListener {
        //void onItemClick(View view, String videoName, Bitmap bitmap);
        void onItemClick(View view, String videoName);

    }

/*
    public void setOnItemClickListener(OnItemClickListener listener) {

        mOnItemClickListener = listener;

    }

 */

    public MyAdapter(Context context, ArrayList<Video> list, OnItemClickListener listener1){
        this.context=context;
        this.list = list;
        this.mOnItemClickListener = listener1;
        //this.mOnMenuClickListener = listener2;
        checkboxList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /*
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.video_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);



        return viewHolder;
         */

        View convertView = LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);
        return new ViewHolder(convertView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //createCheckBoxList();
        //Log.d("position","add size "+checkboxList.size());

        Video video = list.get(position);

        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
        holder.videoImage.setImageBitmap(video.getVideoResource());
        holder.videoName.setText(video.getVideoName());
        holder.duration.setText(video.getDuration());
        //Uri uri = getPath(filename);
        //RequestOptions option1 = new RequestOptions().circleCrop();
        //Glide.with(getApplicationContext()).load(uri).apply(option1).into(play);
        if(position >= checkboxList.size()){
            checkboxList.add(position, false);
            Log.d("position","add position "+position+" isChecked "+"false");
        }

        if(visibility==1) {
            holder.checkBox.setVisibility(View.VISIBLE);
            if(checkboxList.get(position)) {
                holder.isChecked = true;
                holder.checkBox.setChecked(true);
            }
            else {
                holder.isChecked = false;
                holder.checkBox.setChecked(false);
            }
        }
        else {
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.play.setBackground(ContextCompat.getDrawable(getApplicationContext(),list.get(position).getPlayButton()));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.isChecked) {
                    holder.isChecked=false;
                    holder.checkBox.setChecked(false);
                    checkboxList.set(position,false);
                    Log.d("position","position "+position+" isChecked "+"false");
                }
                else {
                    holder.isChecked=true;
                    holder.checkBox.setChecked(true);
                    checkboxList.set(position,true);
                    Log.d("position","position "+position+" isChecked "+"true");
                }
            }
        });

        holder.play.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkBoxFlag==0) {
                    String send = (String) holder.videoName.getText();
                    BitmapDrawable drawable = (BitmapDrawable) holder.videoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    //mOnItemClickListener.onItemClick(v, position);
                    mOnItemClickListener.onItemClick(v, send);
                    //Log.d("Recyclerview", "position = "+position);
                }
                else{
                    if(holder.isChecked) {
                        holder.isChecked=false;
                        holder.checkBox.setChecked(false);
                        checkboxList.set(position,false);
                        Log.d("position","position "+position+" isChecked "+"false");
                    }
                    else {
                        holder.isChecked=true;
                        holder.checkBox.setChecked(true);
                        checkboxList.set(position,true);
                        Log.d("position","position "+position+" isChecked "+"true");
                    }
                }
            }
        });
        holder.videoImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkBoxFlag==0) {
                    String send = (String) holder.videoName.getText();
                    BitmapDrawable drawable = (BitmapDrawable) holder.videoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    mOnItemClickListener.onItemClick(v, send);
                }
                else{
                    if(holder.isChecked) {
                        holder.isChecked=false;
                        holder.checkBox.setChecked(false);
                        checkboxList.set(position,false);
                        Log.d("position","position "+position+" isChecked "+"false");
                    }
                    else {
                        holder.isChecked=true;
                        holder.checkBox.setChecked(true);
                        checkboxList.set(position,true);
                        Log.d("position","position "+position+" isChecked "+"true");
                    }
                }
            }
        });


        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미
                // MenuInflater inf = popup.getMenuInflater();

                popup.getMenuInflater().inflate(R.menu.gallery_item_menu, popup.getMenu());
                popup.show();//Popup Menu 보이기

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.m1:
                                String send = (String) holder.videoName.getText();
                                mOnItemClickListener.onItemClick(v,send);
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });


            }
        });


        //if(trash)
         //   holder.checkBox.setVisibility(View.VISIBLE);
        //else
       //     holder.checkBox.setVisibility(View.GONE);

        /*
        holder.play.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mOnItemClickListener.onItemClick(v, posi);
            }
        });

         */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void turnOffCheckBox(){

    }

    public void updateCheckbox(int n){
        visibility = n;
    }

    //public int getChecked


    public class checkBoxData{
        int id;
        boolean isChecked;
    }

    public void releaseCheckBoxList(){
        checkboxList.clear();
        checkboxList = null;
    }

    public int getCheckBoxSize(){
        return checkboxList.size();
    }

    public boolean getCheckBoxItem(int position){
        return checkboxList.get(position);
    }

    public void removeVideoList(int position){
        list.remove(position);
        Log.d("삭제","video list 삭제 "+position);
    }

    public void removeCheckBoxList(int position){
        checkboxList.remove(position);
        Log.d("삭제","check box list 삭제 "+position);
    }

    public Video getVideo(int position){
        Video temp = list.get(position);
        return temp;
    }

    public void createCheckBoxList(){
        if(checkboxList ==null){
            checkboxList = new ArrayList<>();
        }
    }

    public void initializeCheckBox(){
        for(int i = 0; i < checkboxList.size();i++)
            checkboxList.set(i,false);
    }

    public void setCheckBoxFlag(int b){
        checkBoxFlag = b;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView videoImage;
        ImageView play;
        TextView duration;
        TextView videoName;
        CheckBox checkBox;
        boolean isChecked = false;
        ImageButton menu;


        public ViewHolder(View itemView)
        {
            super(itemView);

            videoImage = itemView.findViewById(R.id.videoImage);
            play = itemView.findViewById(R.id.play);
            duration = itemView.findViewById(R.id.duration);
            videoName = itemView.findViewById(R.id.videoName);
            checkBox = itemView.findViewById(R.id.checkBox);
            menu = itemView.findViewById(R.id.menu);
            /*
            if(visibility==1)
                checkBox.setVisibility(View.VISIBLE);
            else
                checkBox.setVisibility(View.GONE);

             */
/*
            play.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    if(!checkBoxFlag) {
                        String send = (String) videoName.getText();
                        BitmapDrawable drawable = (BitmapDrawable) videoImage.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();

                        //mOnItemClickListener.onItemClick(v, position);
                        mOnItemClickListener.onItemClick(v, send);
                        //Log.d("Recyclerview", "position = "+position);
                    }
                    else{

                    }
                }
            });
            videoImage.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    String send = (String)videoName.getText();
                    BitmapDrawable drawable = (BitmapDrawable) videoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    mOnItemClickListener.onItemClick(v, send);
                }
            });

 */
            /*
            checkBox.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    if(isChecked){
                        isChecked=false;
                        checkBox.setChecked(false);
                    }
                    else{
                        isChecked=true;
                        checkBox.setChecked(true);
                    }
                }
            });

             */

            /*
            checkBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 체크 상태를 계속 확인해서 상태가 변경된다면 리스트뷰 아이템도 갱신하여 준다.
                    checkboxList.isChecked = isChecked;
                }
            });

             */





        }


    }

}

