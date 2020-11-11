package com.yonginuniv.childtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

// 기기 정보 수정 화면을 구성하는 클래스.
public class ModifyDevice extends Activity {
    EditText modify_et_device;
    EditText modify_et_name;
    ImageButton btnBack;
    Button btnModify, btnClearData, btnUnRegister;
    ListView modify_lv;
    ArrayList<Device> devices = new ArrayList<>(); // 기기들의 정보를 담는 배열
    DeviceAdapter adapter;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_device);
        modify_et_device = (EditText) findViewById(R.id.modify_et_device);
        modify_et_name = (EditText) findViewById(R.id.modify_et_name);
        btnBack = (ImageButton) findViewById(R.id.modify_btn_back);
        btnModify = (Button) findViewById(R.id.modify_btn_modify);
        btnClearData = (Button) findViewById(R.id.modify_btn_clearData);
        btnUnRegister = (Button) findViewById(R.id.modify_btn_unRegister);
        modify_lv = (ListView) findViewById(R.id.modify_lv);
        id = getIntent().getStringExtra("id");

        InitAdapter(); // 리스트뷰에 어댑터 연결.

        modify_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트뷰  선택되었을때 : 선택된 디바이스정보를 텍스트뷰에 띄운다.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modify_et_device.setText(devices.get(position).getDevice_serial_number());
                modify_et_name.setText(devices.get(position).getDevice_name());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() { // 창닫기 버튼 누른 경우.
            @Override
            public void onClick(View v) {
                finish();
            }
        }); //닫기 버튼 눌림. 닫아버림.

        btnModify.setOnClickListener(new View.OnClickListener() { // 수정버튼 누른 경우.
            @Override
            public void onClick(View v) {
                // 유효성 검사
                if (modify_et_name.getText().toString().isEmpty())  // 기기별칭 칸을 비운경우.
                    Toast.makeText(getApplicationContext(), "기기 별칭을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    DBConnect task = new DBConnect(); // 서버 통신을 위한 객체 생성.
                    CallBack callBack = new CallBack() { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
                        @Override
                        public void onTaskDone() {
                            if (DBConnect.result.equals("failed")) {
                                Toast.makeText(getApplicationContext(), "수정 실패.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "수정 성공.", Toast.LENGTH_SHORT).show();
                                InitAdapter(); //바뀐정보를 받아왔으니 리스트뷰를 다시 띄운다.
                            }
                        }
                    };
                    task.setCallback(callBack); // 위의 콜백 함수 등록.

                    // DB Connect 클래스에 매개변수로 기기 별칭 수정 요청(modify), 기기번호, 별칭 보내면서 서버와 통신 요청.
                    task.execute("modify", modify_et_device.getText().toString(), modify_et_name.getText().toString());
                }
            }
        });

        btnClearData.setOnClickListener(new View.OnClickListener() { // 데이터 초기화 버튼 누른 경우.
            @Override
            public void onClick(View v) {
                DBConnect task = new DBConnect(); // 서버 통신을 위한 객체 생성.
                CallBack callBack = new CallBack() { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
                    @Override
                    public void onTaskDone() {
                        if (DBConnect.result.equals("succed")) {
                            Toast.makeText(getApplicationContext(), "데이터 초기화 성공.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "데이터 초기화 실패.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                task.setCallback(callBack); // 위의 콜백 함수 등록.

                // DB Connect 클래스에 매개변수로 데이터 초기화 요청(cleardata), 기기번호를 보내면서 서버와 통신 요청.
                task.execute("cleardata", modify_et_device.getText().toString());

            }
        });

        btnUnRegister.setOnClickListener(new View.OnClickListener() { // 연결 해제 버튼 누른 경우.
            @Override
            public void onClick(View v) {
                DBConnect task = new DBConnect(); // 서버 통신을 위한 객체 생성.
                CallBack callBack = new CallBack() { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
                    @Override
                    public void onTaskDone() {
                        if (DBConnect.result.equals("succed")) {
                            Toast.makeText(getApplicationContext(), "연결 해제 성공.", Toast.LENGTH_SHORT).show();
                            InitAdapter();
                        } else {
                            Toast.makeText(getApplicationContext(), "연결 해제 실패.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                task.setCallback(callBack); // 위의 콜백 함수 등록.

                // DB Connect 클래스에 매개변수로 기기연결해제 요청(unregister), 기기번호를 보내면서 서버와 통신 요청.
                task.execute("unregister", modify_et_device.getText().toString());
            }
        });


    }

    void InitAdapter() {
        DBConnect task = new DBConnect(); // 서버 통신을 위한 객체 생성.
        CallBack callBack = new CallBack() { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
            @Override
            public void onTaskDone() {
                if(DBConnect.result.equals("no_device")) {
                    Toast.makeText(getApplicationContext(), "메뉴 - 기기등록에서 기기를 등록해야 사용할 수 있습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    devices = new ArrayList<>();
                    String[] dev = DBConnect.result.split("%"); // 디바이스들의 정보가 "%"으로 구분되어 리턴
                    for (int i = 0; i < dev.length; i++) {
                        String[] info = dev[i].split("&");
                        devices.add(new Device(info[0], info[3])); // device 객체에 저장.
                    }
                    adapter = new DeviceAdapter(devices); // 리스트뷰를 사용하기위해 어댑터 객체 생성.
                    modify_lv.setAdapter(adapter); // 리스트뷰에 어댑터 연결
                    modify_et_device.setText(""); modify_et_name.setText("");
                }
            }
        };
        task.setCallback(callBack); // 위의 콜백 함수 등록.

        // DB Connect 클래스에 매개변수로 기기정보 요청(readdevice),  아이디를 보내면서 서버와 통신 요청.
        task.execute("readdevice", id);
    }

    public class DeviceAdapter extends BaseAdapter {
        ArrayList<Device> items;

        public DeviceAdapter(ArrayList<Device> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DeviceListView view = null;
            if (convertView == null) {
                view = new DeviceListView(getApplicationContext());
            } else {
                view = (DeviceListView) convertView;
            }
            Device item = items.get(position);
            view.setName(item.getDevice_name());
            view.setNumber(item.getDevice_serial_number());
            return view;
        }
    }
}
