package com.jimmy.development.tools;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {

    //TODO 怎么判断能直接获取还是要用线程回调呢？

    public interface IMD5Callback {
        void callback(String md5);
    }

    public static void getMD5(@NonNull final String path, final boolean isFullPackage, @NonNull final IMD5Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String md5 = getMD5(path, isFullPackage);
                callback.callback(md5);
            }
        }).start();


    }

    @Nullable
    public static String getMD5(@NonNull String path, boolean isFullPackage) {
        if (path.equals("")) {
            return null;
        }
        FileInputStream in = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            in = new FileInputStream(path);
            byte buffer[] = new byte[SIZE_1_K];
            int len;
            byte[] result;//128 16  8 --> 32 4

            //这部分耗时
            long time = System.currentTimeMillis();
            if (isFullPackage) {
                while ((len = in.read(buffer, 0, SIZE_1_K)) != -1) {
                    md.update(buffer, 0, len);
                }
            } else {
                int size = in.available();
                byte[] data = new byte[2 * SIZE_1_M];//前后1M
                in.read(data, 0, SIZE_1_M); // 前1m
                int skip = size - (2 * SIZE_1_M);
                in.skip(skip);
                in.read(data, SIZE_1_M, SIZE_1_M); // 后1m
                md.update(data);

            }
            in.close();
            System.out.println("read file and update md : " + (System.currentTimeMillis() - time));
            result = md.digest();

            StringBuilder sb = new StringBuilder();
            int i;
            for (byte b : result) {
                i = b;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(i));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

    }

    /**
     * File的length()方法返回的类型为long，long型能表示的正数最大值为：9223372036854775807，
     * 折算成最大能支持的文件大小为：8954730132868714 EB字节，这个量级将在人类IT发展史上受用很多很多年，
     * 而FileInputStream的avaliable()方法返回值是int，在之前也介绍了最大的表示范围，
     * 所能支持的最大文件大小为：1.99GB，而这个量级我们现在很容易就达到了。
     */
    public static long getFileSize(String path) {
        FileChannel fc = null;
        try {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                fc = fis.getChannel();
                return fc.size();
            } else {
                return -1;
            }
        } catch (FileNotFoundException e) {
            return -1;
        } catch (IOException e) {
            return -1;
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    return -1;
                }
            }
        }
    }


    private static final int SIZE_1_M = 1024 * 1024;
    private static final int SIZE_1_K = 1024;


}