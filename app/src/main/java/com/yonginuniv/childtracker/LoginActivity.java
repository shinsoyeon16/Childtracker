package com.yonginuniv.childtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;

//로그인 화면을 구성하는 클래스.
public class LoginActivity extends Activity {
    Button btnLogin, btnJoin;
    ImageButton btnBack;
    EditText etId, etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnJoin = (Button) findViewById(R.id.login_btn_join);
        btnBack = (ImageButton) findViewById(R.id.login_btn_back);
        etId = (EditText) findViewById(R.id.login_et_id);
        etPassword = (EditText) findViewById(R.id.login_et_password);
        etId.setFilters(new InputFilter[]{new CustomInputFilter(), new InputFilter.LengthFilter(10)}); // 입력문자&수 제한
        etPassword.setFilters(new InputFilter[]{new CustomInputFilter(), new InputFilter.LengthFilter(20)}); // 입력문자&수 제한

        btnLogin.setOnClickListener(new View.OnClickListener() { // 로그인 버튼 클릭됨
            @Override
            public void onClick(View v) {
                // 유효성 검사
                if(etId.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty())  // 빈 항목 있을 때
                    Toast.makeText(getApplicationContext(), "빈 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                else { // 빈 항목 없을 때
                    DBConnect task = new DBConnect(); // 서버에 회원가입 정보를 전송하기 위한 객체 생성.
                    CallBack callBack = new CallBack() { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
                        @Override
                        public void onTaskDone() {
                            if(DBConnect.result.equals("failed")){ // 결과1. 로그인 실패인 경우 :  다시입력하게 입력칸 지워버리기~~
                                Toast.makeText(getApplicationContext(), "정확한 아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                etId.setText(""); etPassword.setText("");
                            } else if(DBConnect.result.split("%")[0].equals("succed")){ // 결과2. 로그인 성공인 경우 : 아디랑 로그인한 날짜 보내서 메인창 띄우기~~
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 메인xml로 이동하기 위해 인텐트 생성.
                                intent.putExtra("id", etId.getText().toString()); // key에 session, value에 로그인 된 아이디 넣은 후 인텐트 보내버리기~~
                                intent.putExtra("date", DBConnect.result.split("%")[1]); // 메인화면(지도)에서 오늘날짜 바로 나오게 로그인한 날짜도 보내버리기~~
                                startActivity(intent);} // 메인화면 실행
                        }};
                    task.setCallback(callBack); // 위의 콜백 함수 등록.

                    // DB Connect 클래스에 매개변수로 로그인 요청(login), 아디, 비번 보내면서 서버와 통신 요청.
                    task.execute("login", etId.getText().toString(), etPassword.getText().toString());
                }
            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {  // 회원가입 버튼 누름.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class); // 인텐트 생성
                startActivity(intent); // join 화면 띄우기
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() { //닫기 버튼 누름.
            @Override
            public void onClick(View v) {
                System.exit(0);
            } // 앱 꺼버리기
        });
    }
}
