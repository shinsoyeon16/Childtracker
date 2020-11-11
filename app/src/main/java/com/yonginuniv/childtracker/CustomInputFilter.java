package com.yonginuniv.childtracker;
import android.text.InputFilter;
import android.text.Spanned;
import java.util.regex.Pattern;

 class CustomInputFilter implements InputFilter { //회원가입 & 로그인시 특수문자 입력방지 기능을 하는 클래스
    @Override
    public CharSequence filter(CharSequence source, int start,
                               int end, Spanned dest, int dstart, int dend) {
        Pattern ps = Pattern.compile("^[-_a-zA-Z0-9]+$"); // 영어와 숫자만 입력가능!

        if(source.equals("") || ps.matcher(source).matches()){
            return source;
        }
        return "";
    }
}