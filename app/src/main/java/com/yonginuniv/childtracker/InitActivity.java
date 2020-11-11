package com.yonginuniv.childtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

// 앱 실행시 맨처음 나타나는 화면 구성 클래스.
public class InitActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init);

        Handler timer = new Handler();
        timer.postDelayed(new Runnable() { // 지정시간만큼 로고를 띄운뒤 로그인화면으로 넘어가게 설정.
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }
}