package com.yonginuniv.childtracker;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;

//회원가입 화면을 구성하는 클래스.
public class JoinActivity extends Activity {
    Button btnJoin;
    ImageButton btnBack;
    EditText etId, etPassword, etPhoneNumber, etUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        btnBack = (ImageButton) findViewById(R.id.join_btn_back);
        btnJoin = (Button) findViewById(R.id.join_btn_join);
        etId = (EditText) findViewById(R.id.join_et_id);
        etPassword = (EditText) findViewById(R.id.join_et_password);
        etPhoneNumber = (EditText) findViewById(R.id.join_et_phoneNumber);
        etUserName = (EditText) findViewById(R.id.join_et_userName);
        etId.setFilters(new InputFilter[]{new CustomInputFilter(), new InputFilter.LengthFilter(10)}); // 입력문자&수 제한
        etPassword.setFilters(new InputFilter[]{new CustomInputFilter(), new InputFilter.LengthFilter(20)}); // 입력문자&수 제한
        etUserName.setFilters(new InputFilter[]{new CustomInputFilter2(), new InputFilter.LengthFilter(20)}); // 입력문자&수 제한
        etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); // 핸드폰 번호 유효성 검증을 위해 입력 포맷 설정

        btnJoin.setOnClickListener(new View.OnClickListener() { // 회원가입 버튼 눌림.
            @Override
            public void onClick(View v) {
                 // 유효성 검사
                    if(etId.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()|| etPhoneNumber.getText().toString().isEmpty()|| etUserName.getText().toString().isEmpty()) // 빈항목이 있는 경우
                        Toast.makeText(getApplicationContext(), "빈 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                    else { // 빈항목이 없는 경우
                        DBConnect task = new DBConnect(); // 서버에 회원가입 정보를 전송하기 위한 객체 생성.
                        CallBack callBack = () -> { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
                            if(DBConnect.result.equals("succed")) { // 결과1. 서버에서 succed 를 리턴한 경우 : 회원가입 완료.
                                Toast.makeText(getApplicationContext(), etId.getText().toString() + "님 회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                finish(); //창 닫아버림.
                            } else if(DBConnect.result.equals("duplicate_id")){ // 결과2. 서버에서 duplicate_id 를 리턴한 경우 :  중복된 아이디.
                                Toast.makeText(getApplicationContext(), "이미 가입된 아이디입니다.", Toast.LENGTH_SHORT).show();
                                etId.setText(""); //아디칸 지워주기/
                            }
                            else { // 결과3. 서버에서 fail 을 리턴한 경우 :  회원가입 실패. 서버상태 확인하기!!!@!@!@!@
                                Toast.makeText(getApplicationContext(), "회원가입 실패. 정보를 다시 입력하시거나 관리자에게 연락하세요.", Toast.LENGTH_SHORT).show();
                            }
                        };
                        task.setCallback(callBack); // 위의 콜백 등록.

                        // DB Connect 클래스에 매개변수로 회원가입 요청(Join),아디,비번,폰번호,이름 보내면서 서버와 통신 요청.
                        task.execute("join", etId.getText().toString(), etPassword.getText().toString(),etPhoneNumber.getText().toString().replace("-",""),etUserName.getText().toString());
                    }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() { // 창닫기 버튼 누른 경우.
            @Override
            public void onClick(View v) {
                finish();
            }
        }); //닫기 버튼 눌림. 닫아버림.
    }
}

