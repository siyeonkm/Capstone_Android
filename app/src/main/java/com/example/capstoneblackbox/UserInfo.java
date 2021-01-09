package com.example.capstoneblackbox;

import io.realm.RealmObject;

//현재 로그인한 유저의 정보를 앱 내에서 담고있는 data class
public class UserInfo extends RealmObject {
    private String id;
    private String pw;
    private int exist;

    public UserInfo(String id, String pw, int exist){
        this.id = id;
        this.pw = pw;
        this.exist = exist;
    }

    public UserInfo(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPw() {
        return pw;
    }

    public int getExist() {
        return exist;
    }

    public void setExist(int exist) {
        this.exist = exist;
    }
}
