package com.doif.ccbebe.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    public static String bitmapToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);

        byte[] b = byteArrayOutputStream.toByteArray();

        String result = Base64.encodeToString(b, Base64.DEFAULT);

        return result;
    }

    public static Bitmap stringToBitmap(String s){
        try{
            byte[] b = Base64.decode(s, Base64.DEFAULT);

            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            return bitmap;
        }catch(Exception e){
            e.printStackTrace();

            return null;
        }
    }
}
