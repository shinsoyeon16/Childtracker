package com.yonginuniv.childtracker;

import android.os.Parcel;
import android.os.Parcelable;

// Device 정보를 담기위한 Device 클래스.
public class Device {
    private String device_serial_number;
    private String device_name;
    public Device(String a, String b){
        super();
        device_serial_number= a;
        device_name = b;
    }
    public Device(){
        super();
    }
    public String getDevice_serial_number(){
        return device_serial_number;
    }
    public String getDevice_name(){
        return device_name;
    }
}
