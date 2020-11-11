package com.yonginuniv.childtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// 메인화면(지도)을 구성하는 클래스.
/* 메소드 호출 순서
 * 1. sendtoServer  : 서버의 기기정보와 위치기록 읽어와서 파싱.
 * 2. setMapPoint : 읽어온 데이터들을 지도에 표시(마커, 폴리라인)
 */
public class MainActivity extends Activity {
    RelativeLayout mapViewContainer;
    String id;
    LocalDateTime date;
    TextView tvDate;
    ImageButton btnMenu, btnPrev, btnNext, btnSelectdate;
    static MapView mapView;

    ArrayList<Trace> traces = new ArrayList<>(); // 위치기록들을 담는 배열
    ArrayList<Device> devices = new ArrayList<>(); // 기기들의 정보를 담는 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      /*  // 키해쉬 확인하는 코드
       try {
            PackageInfo info = getPackageManager().getPackageInfo("com.yonginuniv.childtracker", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray()); String str = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                Log.i("sss", str); Toast.makeText(this, str, Toast.LENGTH_LONG).show();
            }
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
*/ // 키해쉬 확인하는 코드 (디버깅버젼, 릴리즈버젼 앱 만들때 필요)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        id = getIntent().getStringExtra("id"); // loginActivity에서 보낸 id 값 저장.
        date = LocalDateTime.parse(getIntent().getStringExtra("date") + "T00:00:00"); //  loginActivity에서 보낸 오늘날짜를 date 변수에 저장
        tvDate = (TextView) findViewById(R.id.main_tv_date);
        btnMenu = (ImageButton) findViewById(R.id.main_btn_menu);
        btnPrev = (ImageButton) findViewById(R.id.main_btn_prev);
        btnNext = (ImageButton) findViewById(R.id.main_btn_next);
        btnSelectdate = (ImageButton) findViewById(R.id.main_btn_selectdate);
        mapViewContainer = (RelativeLayout) findViewById(R.id.main_map_view);
        traces = new ArrayList<Trace>(); // 위치 데이터들을 담을 traces 배열을 초기화한다.
        devices = new ArrayList<Device>(); //  기기들의 정보를 담을 devices 배열을 초기화한다.


        sendtoServer(date); //  위치데이터를  서버에 요청하는 메소드 호출.

        btnMenu.setOnClickListener(new View.OnClickListener() // 메뉴 버튼 눌렸을때
        {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v); // 팝업메뉴 띄우기
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                Menu menu = popupMenu.getMenu();
                menuInflater.inflate(R.menu.menu, menu); // 메뉴 인플레이터 등록

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // 팝업메뉴 눌렸을때 리스너
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_register: { // 기기 등록 메뉴 눌렸을때
                                Intent intent = new Intent(getApplicationContext(), RegisterDevice.class);
                                intent.putExtra("id", id);
                                startActivityForResult(intent, 1);
                                break;
                            }
                            case R.id.menu_modify: { // 기기 관리 메뉴 눌렸을때
                                Intent intent = new Intent(getApplicationContext(), ModifyDevice.class);
                                intent.putExtra("id", id);
                                startActivityForResult(intent, 1);
                                break;

                            }
                            case R.id.menu_info: { // 개발자 정보 메뉴 눌렸을때
                                Intent intent = new Intent(getApplicationContext(), DeveloperInformationActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.menu_logout: { // 로그아웃 눌렀을때
                                System.exit(0);
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() //이전 버튼이 눌렸을 때
        {
            @Override
            public void onClick(View v) {
                date = date.minusDays(1); // 하루 전으로 날짜 설정.
                sendtoServer(date);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() //다음 버튼이 눌렸을 때
        {
            @Override
            public void onClick(View v) {
                date = date.plusDays(1); // 하루 전으로 날짜 설정.
                sendtoServer(date);
            }
        });
        btnSelectdate.setOnClickListener(new View.OnClickListener() //날짜 선택 버튼이 눌렸을 때
        {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() { // 날짜 선택 다이얼로그 띄우기
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) { // 다이얼로그에서 처리한 후 실행되는 리스너
                        date = LocalDateTime.of(year, month + 1, dayOfMonth, 00, 00, 00, 0000); // 선택한 날짜를 date 변수에 대입.
                        sendtoServer(date); // 바뀐날짜로 지도 다시 그리기.
                    }
                }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).show();  //날짜선택창의 기본 선택 날짜를 현재date 값으로 설정, show()로 화면에 띄우기
            }
        });
    } // onCreate 종료.

    // 서버의 데이터 읽어와서 파싱하는  함수, 파라미터로 date 변수에 저장된 날짜를 보낸다.
    public void sendtoServer(LocalDateTime d) {
        // 서버와 통신하기 전에 기존 정보들 초기화 하기.
        traces.clear();
        devices.clear();

        tvDate.setText(d.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))); // 화면 상단에 날짜 출력.

        DBConnect task = new DBConnect(); // DB 에서 위치데이터 받아오기위해 통신 객체생성
        CallBack callBack = () -> { // 서버 통신 후 실행시킬 콜백 인스턴스 정의
            mapViewContainer.removeAllViews(); // 카카오지도를 xml 의 맵뷰와 연결.
            mapView = new MapView(this);
            mapViewContainer.addView(mapView);

            if (DBConnect.result.equals("no_device")) {           //  결과1. 등록디바이스가 없는 경우
                Toast.makeText(getApplicationContext(), "메뉴 - 기기등록에서 기기를 등록해야 사용할 수 있습니다.", Toast.LENGTH_LONG).show();
            } else if (DBConnect.result.equals("no_data")) {       //  결과2. 선택된 날짜의 데이터가 없는 경우.
                Toast.makeText(getApplicationContext(), "해당 날짜로 조회되는 데이터가 없습니다.", Toast.LENGTH_LONG).show();
            } else {                                                                //  결과3. 데이터받아오기 성공인 경우 : 받아온 결과값 파싱하기

                String[] dev = DBConnect.result.split("#"); // 1. 디바이스들의 정보는 "#"으로 구분되어 리턴
                // ex)  device_serial_number1 & device_name1 # device_serial_number2 & device_name2 # device_serial_number3 & device_name3 # 맨뒤에는 위치정보들....
                for (int i = 0; i < dev.length - 1; i++) {
                    String[] info = dev[i].split("&");
                    devices.add(new Device(info[0], info[1])); // device 객체에 저장.
                }
                String[] arr = dev[dev.length - 1].split("%"); // 2. 위치정보는 "%"로 구분되어 리턴됨
                //ex)  device_serial_number & latitude & longitude & record_time %  device_serial_number & latitude & longitude & record_time % ...
                int i = 0;
                do {
                    String[] arr2 = arr[i].split("&");
                    traces.add(new Trace(arr2[0], arr2[1], arr2[2], arr2[3])); // traces 객체에 저장.
                    i++;
                } while (i < arr.length); // 데이터 파싱 완료.

                Collections.sort(traces, new mySort());  // traces 데이터를 기록된 날짜 순서로 오름차순 정렬시키기. (먼저 기록된게 맨앞으로 가게끔)

                setMapPoint(); // 지도에 marker & polyline 그리는 함수 호출.
            }
        };
        task.setCallback(callBack); //콜백 함수 등록.
        // DB Connect 클래스에 매개변수로 데이터전송요청(readdata), 아디, 기록날짜 보내면서 서버와 통신 요청.
        task.execute("readdata", id, d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")));
    }

    // 서버에서 받아온 데이터들로 marker & polyline 그리기.
    public void setMapPoint() {
        // 서버와 통신하기 전에 기존 정보들 초기화 하기.
        mapView.removeAllPolylines();
        mapView.removeAllPOIItems();

        for (Device d : devices) {  // 기기마다 한줄씩 표시하도록 기기수 만큼 반복문 실행.

            //마커 & 폴리라인 객체생성 및 기본설정
            MapPOIItem marker = new MapPOIItem();
            marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본 마커 색
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 선택된 마커 색
            marker.setTag(0);
            MapPolyline polyline = new MapPolyline();
            polyline.setTag(1000);
            polyline.setLineColor(Color.parseColor("#1267f9")); // Polyline 색 설정  여기서 설정!!!!
            for (Trace t : traces) {
                if (t.getDevice_serial_number().equals(d.getDevice_serial_number())) { // 디바이스에 해당하는 데이터가 있는경우
                    // traces 데이터들을 marker & polyline 등록
                    marker.setItemName(d.getDevice_name() + "\n" + t.getRecordtime().format(DateTimeFormatter.ofPattern("HH시 mm분"))); //marker 명을 디바이스별칭+저장시간으로 설정
                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(t.getLatitude()), Double.parseDouble(t.getLongitude()))); // 마커 좌표 등록
                    mapView.addPOIItem(marker); // 맵뷰에 마커 등록.
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(t.getLatitude()), Double.parseDouble(t.getLongitude()))); //poiyline에 좌표 등록.
                    mapView.addPolyline(polyline); //Polyline 지도에 올리기.
                    // 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    int padding = 100;
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                }
            }
        }
    } // set map point 끝

    class mySort implements Comparator<Trace> {  // 정렬시키기위한 코드.
        @Override
        public int compare(Trace a, Trace b) {
            return a.getRecordtime().compareTo(b.getRecordtime());
        }
    } // 정렬시키기위한 코드.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Register Device 완료됨.
            sendtoServer(date);
        }
    }
}



