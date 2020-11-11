package com.yonginuniv.childtracker;
import org.threeten.bp.LocalDateTime;

// 위치 기록을 저장할 클래스.
public class Trace {
    private String device_serial_number;
    private String latitude;
    private String longitude;
    private LocalDateTime record_time;
    public Trace(String a, String b, String c, String d){
        super();
        device_serial_number= a;
        latitude = b;
        longitude=c;
        this.record_time = LocalDateTime.parse(d, org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public Trace(){
        super();
    }
    public String getDevice_serial_number(){
        return device_serial_number;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getLongitude(){
        return longitude;
    }
    public LocalDateTime getRecordtime(){
        return record_time;
    }
}
