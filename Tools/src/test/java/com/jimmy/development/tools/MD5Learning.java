package com.jimmy.development.tools;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jinguochong on 17-9-8.
 * toHex3，Message Digest Algorithm 5，信息摘要算法，
 * 可以将给定的任意长度数据通过一定的算法计算得出一个 128 位固定长度的散列值。
 * 压缩性：任意长度的原数据，其 toHex3 值都是固定的，即 128 位；
 * 易计算：计算原数据的 toHex3 值是一个比较容易的过程；
 * 抗修改：原数据的任意改动，所得到的 toHex3 值都是迥然不同的；
 * 防碰撞：这一点要特别介绍一下。toHex3 使用的是散列函数（也称哈希函数），一定概率上也存在哈希冲突（也称哈希碰撞），即多个不同的原数据对应一个相同的 toHex3 值。不过，经过 MD4、MD3 等几代算法的优化，toHex3 已经充分利用散列的分散性高度避免碰撞的发生。
 * 可以看出，toHex3 是一种不可逆的算法，也就说，你无法通过得到的 toHex3 值逆向算出原数据内容。正是凭借这些特点，toHex3 被广泛使用。
 * 严格意义上来讲，toHex3 以及 SHA1 并不属于加密算法，也不属于签名算法，而是一种摘要算法，用于数据完整性校验等。
 * 常用的是：给定一串String或者一个文件，得到长度为32位的16进制字符串:      如：md5Str =        "3f72eafdae5955ef4daadad655abb5fedeaf646c"；
 * 或者是长度16位的16进制字符串 —— 从32位里面截取中间的[8-24）位长度:      md5Str.substring(8, 24) = "dae5955ef4daadad655abb5fe";
 */

//bit 位：0/1     byte 字节：=8bit
//一个char 两个字节，一个字节8bit

public class MD5Learning {

    //TODO 修改为自己电脑的一个大文件
    private String path = "/Users/.../AndroidStudioProjects/AdobePhotoshop2017 MAS.dmg";

    @Test
    public void testMethod() {
        MD5Utils.getMD5(path, true, new MD5Utils.IMD5Callback() {
            @Override
            public void callback(String md5) {
                System.out.println("md5:" + md5);
            }
        });
    }

    @Test
    public void explainProcess() throws NoSuchAlgorithmException {

        // 第1步，获取 MessageDigest 对象，参数为 toHex3 字符串，表示这是一个 toHex3 算法（其他还有 SHA1 算法等）：
        //MD5是16位,SHA是20位（这是两种报文摘要的算法）
        MessageDigest md = MessageDigest.getInstance("MD5");


        //第2步，输入原数据，参数类型为 byte[] ：
        byte[] input = "27".getBytes();

        //第2.1步：调用update追加文件
        // 注意：update() 方法有点类似 StringBuilder 对象的 append() 方法，采用的是追加模式，属于一个累计更改的过程：
        //如果文件过大，不能一次全部把文件加入到内存里面
        //所以会采用部分加载的策略 @link MD5Utils.getMD5();
        md.update(input);


        //第3步：转换并返回结果，也是字节数组，包含16个元素，其实是128位"0"和"1"
        byte[] result = md.digest();
        //digest()返回的是128位字节哈希值，这128位"0"和"1"是存在长度为16的byte数组里面，
        // 最后要生成32位的16进制的字符(String)，那么一个byte(8位)对应两个字符(char)
        //通常，我们会转化为十六进制的 32 位长度的字符串来使用，可以利用 BigInteger 类来做这个转化：（错误的）错误的测试用例为：2和28
        //注意：digest() 方法被调用后，MessageDigest 对象就被重置，也就是说你不能紧接着再次调用该方法计算原数据的 toHex3 值。当然，你可以手动调用 reset() 方法重置输入源。
        System.out.println(result.length);

        //最后第4步：将转换后的size为16的byte数组（128bit）转为16进制的字符串
        String md5String = toHex1(result);

        System.out.println("md5String:" + md5String);

        //第4步的错误示范：这是网上常见的toHex4方法
        BigInteger bigInt = new BigInteger(1, result);
        String output1 = bigInt.toString(16);
        System.out.println("output1:" + output1);

    }

    @Test
    public void createTestCase() throws NoSuchAlgorithmException {
        int i = 0;
        String correct = "";
        String err = "";
        while (correct.equals(err)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = String.valueOf(i).getBytes();
            md.update(input);
            byte[] result = md.digest();
            correct = toHex3(result);//这个是对的
            err = toHex4(result);//只要把这个换成想要测试的可能方法就行

            i++;

        }

        System.out.println("i:" + i);
    }

    @Test
    public void testCommonMethods() throws NoSuchAlgorithmException {
        //以下是三种正确转换和1种常用错误转换
        //不耗时
        String md5_1;
        String md5_2;
        String md5_3;
        String md5_err1;
        String md5_err2;

        int[] testCases = new int[]{1, 2, 27};
        for (int testCase : testCases) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = String.valueOf(testCase).getBytes();
            md.update(input);
            byte[] result = md.digest();

            md5_1 = toHex1(result);
            md5_2 = toHex2(result);
            md5_3 = toHex3(result);
            //把 toHex3 的补0部分注释
            md5_err1 = MD5Without0(result);
            //网上常见错误
            md5_err2 = toHex4(result);

            //出错在1，2
            System.out.println(md5_1);
            System.out.println(md5_2);
            System.out.println(md5_3);
            System.out.println(md5_err1);
            System.out.println();

            //取中间16位的情况，出错在27
            System.out.println(md5_1.substring(8, 24));
            System.out.println(md5_2.substring(8, 24));
            System.out.println(md5_3.substring(8, 24));
            System.out.println(" " + md5_err2.substring(8, 24));//前面加个空格方便看
            System.out.println("-------------------------------------------");
            System.out.println();

        }

    }


    private static String toHex1(byte[] result) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < result.length; i++) {
            //高四位移动到低四位,高位是1则高四位补1，高位是0则高四位补0,故还需要&00001111
            int hi = (result[i] >> 4) & 0x0f;
            int lo = result[i] & 0x0f;
            sb.append(hi > 9 ? (char) (hi - 10 + 'a') : (char) (hi + '0'));
            sb.append(lo > 9 ? (char) (lo - 10 + 'a') : (char) (lo + '0'));
        }
        return sb.toString()/*.substring(8, 24)*/;
    }

    private static String toHex2(byte[] result) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            //高四位移动到低四位,高位是1则高四位补1，高位是0则高四位补0,故还需要&00001111
            int hi = (result[i] >> 4) & 0x0f;
            int lo = result[i] & 0x0f;
            sb.append(Integer.toHexString(hi));
            sb.append(Integer.toHexString(lo));
        }
        return sb.toString()/*.substring(8, 24)*/;
    }

    private static String toHex3(byte[] result) {
        String output;
        int i;
        StringBuilder buf = new StringBuilder("");
        for (int offset = 0; offset < result.length; offset++) {
            i = result[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        output = buf.toString();
        return output/*.substring(8, 24)*/;
    }

    //错误示范，高位不补0的情况：把 toHex3 的补0部分注释：见注释代码，错误出现在 "1","2","27"
    private static String MD5Without0(byte[] result) {
        String output;
        int i;
        StringBuilder buf = new StringBuilder("");
        for (int offset = 0; offset < result.length; offset++) {
            i = result[offset];
            if (i < 0)
                i += 256;
            //错误示范：这里注释的代码
            //if (i < 16)
            //    buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        output = buf.toString();
        return output/*.substring(8, 24)*/;
    }

    //错的，测试用例为27
    private static String toHex4(byte[] result) {
        BigInteger bigInt = new BigInteger(1, result);
        String err = bigInt.toString(16);
        return err/*.substring(8, 24)*/;
    }


}