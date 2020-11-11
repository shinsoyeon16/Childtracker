package com.yonginuniv.childtracker;

import android.text.InputFilter;
import android.text.Spanned;
import java.util.regex.Pattern;

public class CustomInputFilter2 implements InputFilter {  //회원가입 & 로그인시 특수문자 입력방지 기능을 하는 클래스
    @Override
    public CharSequence filter(CharSequence source, int start,
                               int end, Spanned dest, int dstart, int dend) {
        Pattern ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-힣]+$"); // 영어와 숫자와 한글만 입력가능!

        if(source.equals("") || ps.matcher(source).matches()){
            return source;
        }
        return "";
    }
}