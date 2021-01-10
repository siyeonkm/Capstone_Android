package com.example.capstoneblackbox;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

//인터넷 설정창에서 흔히 보던 그 쿠키데이터 어쩌구 맞음
//client의 여러가지 정보들을 담아주고 세션 간 연결해주는 역할!
//activity 클래스 아님 다른 activity class에서 선언해서 쓰는 용도
public class MyCookieJar implements CookieJar {

    private List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies =  cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (cookies != null)
            return cookies;
        return new ArrayList<Cookie>();

    }
}
