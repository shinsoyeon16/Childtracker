package com.yonginuniv.childtracker;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

// 기기등록 화면을 구성하는 클래스.
public class RegisterDevice extends Activity {
    Button btnRegister;
    ImageButton btnBack;
    EditText etDevice, etDeviceName;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_device);
        btnBack = (ImageButton) findViewById(R.id.register_btn_back);
        btnRegister = (Button) findViewById(R.id.register_btn_register);
        etDevice = (EditText) findViewById(R.id.register_et_device);
        etDeviceName = (EditText) findViewById(R.id.register_et_device_name);
        etDeviceName.setFilters(new InputFilter[]{new CustomInputFilter2(), new InputFilter.LengthFilter(20)}); // 입력문자&수 제한
        id = getIntent().getStringExtra("id"); // mainActivity 에서 보낸 id 저장.

        etDevice.addTextChangedListener(new TextWatcher() { //device Serial Number 유효성 검증. 입력된 수에 따라 자동하이픈추가 (숫자 4 개씩 묶어버리기~~)
            private int _beforeLenght = 0;
            private int _afterLenght = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                _beforeLenght = s.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    return;
                }
                char inputChar = s.charAt(s.length() - 1);
                if (inputChar != '-' && (inputChar < '0' || inputChar > '9')) {
                    etDevice.getText().delete(s.length() - 1, s.length());
                    return;
                }
                _afterLenght = s.length();
                // 삭제 중
                if (_beforeLenght > _afterLenght) {
                    // 삭제 중에 마지막에 -는 자동으로 지우기
                    if (s.toString().endsWith("-")) {
                        etDevice.setText(s.toString().substring(0, s.length() - 1));
                    }
                }
                // 입력 중
                else if (_beforeLenght < _afterLenght) {
                    if (_afterLenght == 5 && s.toString().indexOf("-") < 0) {
                        etDevice.setText(s.toString().subSequence(0, 4) + "-" + s.toString().substring(4, s.length()));
                    } else if (_afterLenght == 10) {
                        etDevice.setText(s.toString().subSequence(0, 9) + "-" + s.toString().substring(9, s.length()));
                    } else if (_afterLenght == 15) {
                        etDevice.setText(s.toString().subSequence(0, 14) + "-" + s.toString().substring(14, s.length()));
                    }
                }
                etDevice.setSelection(etDevice.length());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {  // 기기등록 버튼 눌림.
            @Override
            public void onClick(View v) {
                 //유효성 검사하기
                    if(etDevice.getText().toString().isEmpty() || etDeviceName.getText().toString().isEmpty())
                        Toast.makeText(getApplicationContext(), "빈 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                    else { //유효성 검사 통과한 경우
                        DBConnect task = new DBConnect();
                        CallBack callBack = new CallBack() { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
                            @Override
                            public void onTaskDone() {
                                if(DBConnect.result.equals("succed")) { // 결과1. 기기등록 성공.
                                    Toast.makeText(getApplicationContext(), "기기등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    setResult(1); finish(); // 창닫고 메인화면으로 돌아감.
                                } else if(DBConnect.result.equals("duplicate_serial")) { // 결과2. 기기번호 중복.
                                    Toast.makeText(getApplicationContext(), "정확한 일련번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                } else { // 결과3. 기기등록 실패. 서버상태 확인하기!!!@!@!@!@
                                    Toast.makeText(getApplicationContext(), "기기등록 실패. 정보를 다시 입력하시거나 관리자에게 연락하세요.", Toast.LENGTH_SHORT).show();
                                }
                            }};
                        task.setCallback(callBack); //콜백 등록.

                        // DB Connect 클래스에 매개변수로 기기등록 요청(register),아디, 기기번호, 기기이름 보내면서 서버와 통신 요청.
                        task.execute("register", id, etDevice.getText().toString(), etDeviceName.getText().toString());
                    }
                }

        });

        btnBack.setOnClickListener(new View.OnClickListener() { //닫기 버튼 눌림.
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

