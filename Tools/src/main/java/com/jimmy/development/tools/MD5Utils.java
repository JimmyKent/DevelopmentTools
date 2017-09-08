package com.jimmy.development.tools;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 功能：支付宝MD5签名处理核心文件，不需要修改 版本：3.3 修改日期：2012-08-17 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个
 */

public class MD5Utils {

    private static final int SIZE_1_M = 1024 * 1024;

    public static String sign(String text) {
        return new String(HexUtils.encodeHex(DigestUtils.md5(getContentBytes(text, "utf-8"))));
    }

    public static String getFileMD5(File file, boolean isFullPackage) {
        if (!file.isFile() || !file.exists()) {
            return null;
        }
        if (isFullPackage) {
            MessageDigest digest = null;
            FileInputStream in = null;
            byte buffer[] = new byte[1024];
            int len;
            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);
                while ((len = in.read(buffer, 0, 1024)) != -1) {
                    digest.update(buffer, 0, len);
                }
                in.close();
            } catch (Exception e) {
                Log.w("MD5Utils", e);
                return null;
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } else {
            MessageDigest digest;
            FileInputStream in = null;
            byte[] data = new byte[2 * SIZE_1_M];
            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);
                int size = in.available();

                in.read(data, 0, SIZE_1_M); // 前1m
                int skip = size - (2 * SIZE_1_M);
                in.skip(skip);
                in.read(data, SIZE_1_M, SIZE_1_M); // 后1m
                digest.update(data);
                BigInteger bigInt = new BigInteger(1, digest.digest());
                return bigInt.toString(16);
            } catch (Exception e) {
                Log.w("MD5Utils", e);
                return null;
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.w("MD5Utils", e);
                }
            }
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws java.security.SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (TextUtils.isEmpty(charset)) {
            return content.getBytes();
        }

        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
                    + charset);
        }
    }

}