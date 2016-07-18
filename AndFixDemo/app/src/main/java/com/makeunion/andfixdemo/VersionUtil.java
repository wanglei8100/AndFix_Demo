package com.makeunion.andfixdemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by renjialiang on 2016/5/13.
 */
public class VersionUtil {

    /**
     * Get the version of APK
     * @param context context
     * @return apkVersion
     */
    public static String getApkVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

}
