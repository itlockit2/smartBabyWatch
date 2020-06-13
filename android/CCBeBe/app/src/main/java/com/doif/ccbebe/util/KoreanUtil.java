package com.doif.ccbebe.util;

// 이름에 따라 이/가 , 을/를
public class KoreanUtil {

    public static String getComleteWord(String name, String value1, String value2){

        char lastName = name.charAt(name.length() - 1);

        // 한글의 제일 처음과 끝의 범위밖일 경우는 오류
        if (lastName < 0xAC00 || lastName > 0xD7A3)
            return name;

        String seletedValue = (lastName - 0xAC00) % 28 > 0 ? value1 : value2;

        return name+seletedValue;
    }
}
