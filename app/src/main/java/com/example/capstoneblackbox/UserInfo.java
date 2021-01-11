package com.example.capstoneblackbox;

import io.realm.RealmObject;

//현재 로그인한 유저의 정보를 앱 내에서 담고있는 data class
//근데 아직까지 쓸모는 없음 혹시 로컬저장소에 저장해놓아야할 유저정보 있을까봐 만들어놓음
public class UserInfo extends RealmObject {
    private String id;
    private String full_video;
    private String date;
    private String size;
    private String storage_path;
    private String user_id;

    public UserInfo(String id, String video, String date, String size, String path, String user){
        this.id = id;
        this.full_video = video;
        this.date = date;
        this.size = size;
        this.storage_path = path;
        this.user_id = user;
    }

    public UserInfo(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStorage_path() {
        return storage_path;
    }

    public void setStorage_path(String storage_path) {
        this.storage_path = storage_path;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
