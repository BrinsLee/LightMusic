package com.brins.lightmusic.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class MachineUtils {

    /**
     * 获取当前进程名
     * @param context
     * @return
     */
    public static String getCurrProcessName(Context context) {
        try {
            final int currProcessId = android.os.Process.myPid();
            final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            if (processInfos != null) {
                for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                    if (info.pid == currProcessId) {
                        return info.processName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
