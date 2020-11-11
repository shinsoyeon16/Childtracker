package com.yonginuniv.childtracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DeviceListView extends LinearLayout {
    TextView lv_tv_devicenumber;
    TextView lv_tv_devicename;
    public DeviceListView(Context context) {
        super(context);
        init(context);
    }
    public DeviceListView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        inflater.inflate(R.layout.listview_custom, this, true);
         lv_tv_devicenumber = (TextView) findViewById(R.id.lv_tv_devicenumber);
         lv_tv_devicename = (TextView) findViewById(R.id.lv_tv_devicename);
    }
    public void setNumber(String number){
        lv_tv_devicenumber.setText(number);
    }
    public void setName(String name){
        lv_tv_devicename.setText(name);
    }
}
