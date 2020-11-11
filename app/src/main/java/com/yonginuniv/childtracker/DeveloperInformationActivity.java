package com.yonginuniv.childtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.Nullable;

//개발자 정보 화면을 구성하는 클래스
public class DeveloperInformationActivity extends Activity {
    ImageButton btn_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_information);

        btn_back = (ImageButton) findViewById(R.id.developer_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() { //닫기 버튼 눌렀을때.
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
