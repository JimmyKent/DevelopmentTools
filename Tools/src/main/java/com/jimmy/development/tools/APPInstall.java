package com.jimmy.development.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;


public class APPInstall {


    /**
     * 安装
     * @param context 接收外部传进来的context
     */
    public static void install(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean hasInstall(Context context, String pkgName, int serverVC) {
        boolean hasInstall = false;
        int localVC = Utility.getAppVersionCode(pkgName, context);
        if (localVC >= serverVC) {
            hasInstall = true;
        }
        return hasInstall;
    }


}
