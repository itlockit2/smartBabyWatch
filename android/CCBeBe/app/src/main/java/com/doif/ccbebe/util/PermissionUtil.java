package com.doif.ccbebe.util;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionUtil {

    boolean requestPermsiion(Activity activity, int requestCode, String... permissions){

        boolean granted = true;
        ArrayList<String> permissionNeeded = new ArrayList<String>();

        for (String it: permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, it);
            boolean hasPermission = permissionCheck == PackageManager.PERMISSION_GRANTED;

            granted = granted && hasPermission;

            if(!hasPermission){
                permissionNeeded.add(it);
            }
        }

        if(granted) return true;
        else{
            ActivityCompat.requestPermissions(activity, permissionNeeded.toArray(new String[0]), requestCode);
            return false;
        }
    }

    boolean permissionGranted(int requestCode, int permissionCode, int[] grantedResults){
        return requestCode == permissionCode && grantedResults.length > 0 && grantedResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
