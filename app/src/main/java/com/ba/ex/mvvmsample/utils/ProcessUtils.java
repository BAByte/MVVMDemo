package com.ba.ex.mvvmsample.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class ProcessUtils {

    private static final String TAG = "ProcessUtils";

    private static String getCurrentProcessName(Context context){
        int pid = android.os.Process.myPid();
        Log.d(TAG,"getCurrentProcessName current pid is " + pid);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(manager != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    Log.d(TAG, "getCurrentProcessName return " + processInfo.processName);
                    return processInfo.processName;
                }
            }
        }
        return "";
    }

    public static boolean isMainProcessInApplication(Context context){
        return TextUtils.equals(getCurrentProcessName(context), context.getPackageName());

    }

}