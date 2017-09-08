package com.jimmy.development.logger.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.jimmy.development.tools.MD5Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static String TAG = "FileUtil";

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * 读取File中的内容
     *
     * @param file 请务必保证file文件已经存在
     * @return file中的内容
     */
    public static String getText(File file) {
        if (!file.exists()) {
            return null;
        }
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            }
        }
        return text.toString();
    }

    /**
     * 遍历获取Log文件夹下的所有crash文件
     *
     * @param logdir 从哪个文件夹下找起
     * @return 返回crash文件列表
     */
    public static ArrayList<File> getCrashList(File logdir) {
        ArrayList<File> crashFileList = new ArrayList<>();
        findFiles(logdir.getAbsolutePath(), crashFileList);
        return crashFileList;
    }


    /**
     * 将指定文件夹中满足要求的文件存储到list集合中
     *
     * @param f
     * @param list
     */

    /**
     * 递归查找文件
     *
     * @param baseDirName 查找的文件夹路径
     * @param fileList    查找到的文件集合
     */
    public static void findFiles(String baseDirName, List<File> fileList) {
        File baseDir = new File(baseDirName);       // 创建一个File对象
        if (!baseDir.exists() || !baseDir.isDirectory()) {  // 判断目录是否存在
            Log.e(TAG, "文件查找失败：" + baseDirName + "不是一个目录！");
        }
        String tempName;
        //判断目录是否存在
        File tempFile;
        File[] files = baseDir.listFiles();
        for (File file : files) {
            tempFile = file;
            if (tempFile.isDirectory()) {
                findFiles(tempFile.getAbsolutePath(), fileList);
            } else if (tempFile.isFile()) {
                tempName = tempFile.getName();
                if (tempName.contains("Crash")) {
                    // 匹配成功，将文件名添加到结果集
                    fileList.add(tempFile.getAbsoluteFile());
                }
            }
        }
    }

    /**
     * 获取文件夹的大小
     *
     * @param directory 需要测量大小的文件夹
     * @return 返回文件夹大小，单位byte
     */
    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    public static File createFile(File zipdir, File zipfile) {
        if (!zipdir.exists()) {
            boolean result = zipdir.mkdirs();
            Log.d("TAG", "zipdir.mkdirs() = " + result);
        }
        if (!zipfile.exists()) {
            try {
                boolean result = zipfile.createNewFile();
                Log.d("TAG", "zipdir.createNewFile() = " + result);
            } catch (IOException e) {
                Log.w(TAG, e);
                Log.e("TAG", e.getMessage());
            }
        }
        return zipfile;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        return file.exists();
    }

    public static boolean createFolder(String folderName) {
        File cacheLocation = new File(folderName);
        if (!cacheLocation.exists()) {
            return cacheLocation.mkdirs();
        }
        return true;
    }

    /**
     * @param file   文件
     * @param md5    服务端返回的MD5
     * @param length 文件大小
     * @return 是否匹配
     */
    public static boolean isMatch(final File file, String md5, long length) {
        Log.e("FileUtil","md5:" + md5   + "   length:" + length);
        int bufLen = 1024 * 100;
        if (file == null || bufLen <= 0 || !file.exists()) {
            return false;
        }
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            String fmd5 = MD5Util.getMD5(fin, (int) (bufLen <= file.length() ? bufLen : file.length()));
            if (md5.equals(fmd5) && fin.getChannel().size() == length) {
                fin.close();
                return true;
            }
        } catch (Exception e) {

        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
            }
        }
        return false;
    }
}