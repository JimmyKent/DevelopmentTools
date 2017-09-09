package com.jimmy.development.tools;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jinguochong on 17-9-8.
 * MD5，Message Digest Algorithm 5，信息摘要算法，
 * 可以将给定的任意长度数据通过一定的算法计算得出一个 128 位固定长度的散列值。
 * 压缩性：任意长度的原数据，其 MD5 值都是固定的，即 128 位；
 * 易计算：计算原数据的 MD5 值是一个比较容易的过程；
 * 抗修改：原数据的任意改动，所得到的 MD5 值都是迥然不同的；
 * 防碰撞：这一点要特别介绍一下。MD5 使用的是散列函数（也称哈希函数），一定概率上也存在哈希冲突（也称哈希碰撞），即多个不同的原数据对应一个相同的 MD5 值。不过，经过 MD4、MD3 等几代算法的优化，MD5 已经充分利用散列的分散性高度避免碰撞的发生。
 * 可以看出，MD5 是一种不可逆的算法，也就说，你无法通过得到的 MD5 值逆向算出原数据内容。正是凭借这些特点，MD5 被广泛使用。
 * 严格意义上来讲，MD5 以及 SHA1 并不属于加密算法，也不属于签名算法，而是一种摘要算法，用于数据完整性校验等。
 */

//bit 位：0/1     byte 字节：=8bit
//一个char 两个字节，一个字节8bit

public class MD5Learning {

    //private String path = "/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/Tools/TODO.txt";
    private String path = "/Users/jinguochong/AndroidStudioProjects/AdobePhotoshop2017 MAS.dmg";

    @Test
    public void test2() {
        MD5Utils.getMD5(path, true, new MD5Utils.IMD5Callback() {
            @Override
            public void callback(String md5) {
                System.out.println("md5:" + md5);
            }
        });
    }

    @Test
    public void testMD5() throws NoSuchAlgorithmException {
        // 第一步，获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
        //MD5是16位,SHA是20位（这是两种报文摘要的算法）
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        //第二步，输入原数据，参数类型为 byte[] ：
        byte[] inputByteArray = "test".getBytes();
        //注意：update() 方法有点类似 StringBuilder 对象的 append() 方法，采用的是追加模式，属于一个累计更改的过程：
        //如果文件过大，不能一次全部把文件加入到内存里面
        //所以会采用部分加载的策略
        messageDigest.update(inputByteArray);
        // 转换并返回结果，也是字节数组，包含16个元素，其实是128位
        //注意：digest() 方法被调用后，MessageDigest 对象就被重置，也就是说你不能紧接着再次调用该方法计算原数据的 MD5 值。当然，你可以手动调用 reset() 方法重置输入源。
        //digest() 方法返回值是一个字节数组类型的 16 位长度的哈希值，通常，我们会转化为十六进制的 32 位长度的字符串来使用，可以利用 BigInteger 类来做这个转化：
        long time = System.currentTimeMillis();
        byte[] resultByteArray = messageDigest.digest();
        //System.out.println("digest time : " + (System.currentTimeMillis() - time));
        //通常，我们会转化为十六进制的 32 位长度的字符串来使用，可以利用 BigInteger 类来做这个转化：（错误的）
        //BigInteger bigInt = new BigInteger(1, resultByteArray);
        //String output1 = bigInt.toString(16);


        //digest()返回的是128位字节，这128位"0"和"1"是存在长度为16的byte数组里面，
        // 最后要生成32位的16进制的字符(String)，那么一个byte(8位)对应两个字符(char)
        //
        // 如果要16位，是取中间的8-24位

        /*for (String s : input) {

            toHex2(s);
            System.out.println("----------------------");
            MD5(s);
            System.out.println("    ");
        }*/

        //不耗时
        String md50 = "";//补0
        String md5 = "";
        int i = 0;
        time = System.currentTimeMillis();
        while (md50.equals(md5)) {
            md5 = MD5(i + "");
            md50 = toHex2(i + "");
            i++;
            System.out.println(md50);
            System.out.println(md5);
            System.out.println("i:" + i);//2中间有0  28首位有0
            System.out.println("    ");


            if (i == 30) {
                break;
            }
        }
        System.out.println("digest time : " + (System.currentTimeMillis() - time));
    }




    private static String toHex2(String sourceStr) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");//SHA-1
        digest.update(sourceStr.getBytes());
        byte[] buf = digest.digest();
        StringBuilder sBuilder = new StringBuilder();

        for (int i = 0; i < buf.length; i++) {
            //高四位移动到低四位,高位是1则高四位补1，高位是0则高四位补0,故还需要&00001111
            int hi = (buf[i] >> 4) & 0x0f;
            int lo = buf[i] & 0x0f;
            sBuilder.append(Integer.toHexString(hi));
            sBuilder.append(Integer.toHexString(lo));
        }
        //System.out.println("MD5(" + sBuilder.toString() + ",32) = " + sBuilder.toString());
        //System.out.println("MD5(" + sBuilder.toString() + ",16) = " + sBuilder.toString().substring(8, 24));
        return sBuilder.toString()/*.substring(8, 24)*/;
    }

    //3f72eafdae5955ef4daadad655abb5fedeaf646c
    //转16进制的字符串形式
    /*private static String toHex(byte[] buf) {
        StringBuilder sBuilder = new StringBuilder();
        System.out.println(buf.length);
        for (int i = 0; i < buf.length; i++) {
            //高四位移动到低四位,高位是1则高四位补1，高位是0则高四位补0,故还需要&00001111
            int hi = (buf[i] >> 4) & 0x0f;
            int lo = buf[i] & 0x0f;
            sBuilder.append(hi > 9 ? (char) (hi - 10 + 'a') : (char) (hi + '0'));
            sBuilder.append(lo > 9 ? (char) (lo - 10 + 'a') : (char) (lo + '0'));
        }
        //System.out.println("MD5(" + ",32) = " + sBuilder.toString());
        //System.out.println("MD5(" + ",16) = " + sBuilder.toString().substring(8, 24));
        return sBuilder.toString().substring(8, 24);
    }*/


    //不补0
    private static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result/*.substring(8, 24)*/;
    }

    //错的
    private static String MD5BigInteger(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            BigInteger bigInt = new BigInteger(1, b);
            result = bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result/*.substring(8, 24)*/;
    }




}