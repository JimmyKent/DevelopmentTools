package com.jimmy.development.tools;

import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by jinguochong on 17-3-15.
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static boolean deleteFile(String fileFullName) {
        File file = new File(fileFullName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    public static void delCurDirAllFile(File file) {
        File[] childFile = file.listFiles();
        if (childFile != null) {//清理缓存
            for (File f : childFile) {
                f.delete();
            }
        }
    }

    public static void createNomediaFile(String path) {
        File nomediaFile = new File(path + File.separatorChar + ".nomedia");
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                Log.w(TAG, e);
            }
        }
    }
}
