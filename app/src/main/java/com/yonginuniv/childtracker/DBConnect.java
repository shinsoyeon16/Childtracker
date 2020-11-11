package com.yonginuniv.childtracker;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// 앱과 서버의 DB 전송을 담당하는 클래스
// 비동기 방식으로 처리하기 위해 AsyncTask 클래스를 상속하여 사용
public class DBConnect extends AsyncTask<String, Integer, String> {
    //  변수선언부
    public static String result = "";
    String ip = "http://yonginchildtracker.ddns.net/"; // 서버 주소를 담는 변수.
    String uri = ""; // 요청값에 따라 달라지는 URI 값이 저장될 변수.
    CallBack callBack;
    void setCallback(CallBack callback){
        this.callBack = callback;
    } //외부에서 DBconnect 를 통해 콜백함수를 지정.

    // 1번째로 실행됨. DB와 통신하는 코드.
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        findMode(params); // login. join 등 어떤 요청인지에 따라 uri 와 parameter를 설정하는 함수 실행.
        try {
            URL url = new URL(ip+uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                String line;
                while ((line = reader.readLine())!= null) {
                    builder.append(line + "\n");
                }
                builder.delete(builder.length()-1,builder.length());
                reader.close();
            }
            conn.disconnect();
            Log.i("sss","통신완료 "+url.toString()+" , "+builder.toString());
            return builder.toString();
        } catch(MalformedURLException e) {
            Log.i("sss", "DBConnect 오류남1: "+e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.i("sss", "DBConnect 오류남2 :"+e);
            e.printStackTrace();
        } return "";
    }

    // 2번째로 실행됨. 결과를 반환하고 콜백함수가 있다면 실행시키는 코드.
    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        result = str;
        if(callBack != null) {
            callBack.onTaskDone(); //설정된 콜백함수 실행.
        }
    }

    // 어떤 요청인지에 따라서 php페이지와 전달값을 분기하는 함수.
    void findMode(String... params) {
        switch (params[0]) {
            case "login": { //login.php 에 id & password 값을 보내 로그인 succed & failed 값을 받아온다.
                uri="login.php?id="+params[1]+"&password="+params[2];
                break;
            }
            case "join": { // 가입자 정보를 전달하여 succed & duplicate_id(중복된아이디) & failed 값을 받아온다.
                uri="join.php?id="+params[1]+"&password="+params[2]+"&phone_number="+params[3]+"&name="+params[4];
                break;
            }
            case "register" : { // Device Serial Number 의 유효& 등록가능 여부를 확인하여 DB에 등록 후 succed & failed 값을 받아옴.
                uri="registerdevice.php?id="+params[1]+"&device_serial_number="+params[2]+"&device_name="+params[3];
                break;
            }
            case "modify" : {
                uri="modifydevice.php?device_serial_number="+params[1]+"&device_name="+params[2];
                break;
            }
            case "cleardata" : {
                uri="cleardata.php?device_serial_number="+params[1];
                break;
            }
            case "unregister" : {
                uri="unregister.php?device_serial_number="+params[1];
                break;
            }
            case "readdevice": {
                uri="readdevice.php?id="+params[1];
                break;
            }
            case "readdata":{ // 요청하는날짜의 위치데이터를 읽어온다.
                uri = "readdata.php?id="+params[1]+"&date="+params[2];
                break;
            }
           default:
       }
    }
}
