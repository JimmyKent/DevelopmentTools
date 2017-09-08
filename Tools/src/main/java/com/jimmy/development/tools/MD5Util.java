package com.jimmy.development.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 描述：转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     * @since v0.1
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i], true));
        }
        return resultSb.toString();
    }

    public static String byteArrayToHexString(byte[] b, int beginPos, int length) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = beginPos; i < length + beginPos; i++) {
            resultSb.append(byteToHexString(b[i], true));
        }
        return resultSb.toString();
    }

    public static String byteArrayToHexStringLittleEnding(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i], false));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b, boolean bigEnding) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return (bigEnding) ? (hexDigits[d1] + hexDigits[d2]) : (hexDigits[d2] + hexDigits[d1]);
    }

    public static String MD5Encode(String origin) {
        return MD5Encode(origin, null);
    }

    /**
     * 把16进制字符串转换为byte数组
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        if (s.length() % 2 != 0) {
            throw new RuntimeException("Error Hex String length");
        }
        byte[] result = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); ) {
            int bytepos = i / 2;
            char c = s.charAt(i++);
            char c2 = s.charAt(i++);
            result[bytepos] = Integer.decode("0x" + c + c2).byteValue();
        }
        return result;
    }

    /**
     * MD5摘要
     *
     * @param origin   摘要原文
     * @param encoding 字符串origin 的编码
     * @return
     */
    public static String MD5Encode(String origin, String encoding) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (encoding == null) {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(encoding)));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultString;
    }

    public static byte[] MD5Encode(byte[] origin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(origin);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toMd5(byte[] bytes, boolean upperCase) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            return toHexString(algorithm.digest(), "", upperCase);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把二进制byte数组生成十六进制字符串，单个字节小于0xf，高位补0。
     *
     * @param bytes     输入
     * @param separator 分割线
     * @param upperCase true：大写， false 小写字符串
     * @return 把二进制byte数组生成十六进制字符串，单个字节小于0xf，高位补0。
     */
    private static String toHexString(byte[] bytes, String separator,
                                      boolean upperCase) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(0xFF & b); // SUPPRESS CHECKSTYLE
            if (upperCase) {
                str = str.toUpperCase();
            }
            if (str.length() == 1) {
                hexString.append("0");
            }
            hexString.append(str).append(separator);
        }
        return hexString.toString();
    }

    /**
     * Get the md5 for the file, using less memory.
     */
    public static String getMD5(final String file) {
        if (file == null) {
            return null;
        }

        File f = new File(file);
        if (f.exists()) {
            return getMD5(f, 1024 * 100);
        }
        return null;
    }

    /**
     * 大文件耗时会比较长
     * Get the md5 for the file, using less memory.
     */
    public static String getMD5(final File file) {
        return getMD5(file, 1024 * 100);
    }


    /**
     * Get the md5 for the file. call getMD5(FileInputStream is, int bufLen) inside.
     *
     * @param file
     * @param bufLen bytes number read from the stream once.
     *               The less bufLen is the more times getMD5() method takes. Also the less bufLen cost less memory.
     */
    public static String getMD5(final File file, final int bufLen) {
        if (file == null || bufLen <= 0 || !file.exists()) {
            return null;
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            String md5 = MD5Util.getMD5(fin, (int) (bufLen <= file.length() ? bufLen : file.length()));
            fin.close();
            return md5;

        } catch (Exception e) {
            return null;

        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {

            }
        }
    }


    /**
     * Get the md5 for inputStream.
     * This method cost less memory. It read bufLen bytes from the FileInputStream once.
     *
     * @param is
     * @param bufLen bytes number read from the stream once.
     *               The less bufLen is the more times getMD5() method takes. Also the less bufLen is the less memory cost.
     */
    public static String getMD5(final InputStream is, final int bufLen) {
        if (is == null || bufLen <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            StringBuilder md5Str = new StringBuilder(32);

            byte[] buf = new byte[bufLen];
            int readCount = 0;
            while ((readCount = is.read(buf)) != -1) {
                md.update(buf, 0, readCount);
            }

            byte[] hashValue = md.digest();

            for (int i = 0; i < hashValue.length; i++) {
                md5Str.append(Integer.toString((hashValue[i] & 0xff) + 0x100, 16).substring(1));
            }
            return md5Str.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
