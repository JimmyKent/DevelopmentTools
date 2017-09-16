package com.jimmy.development.iolib;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**
 * http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly
 * 看api
 * XXX 考虑线程安全
 */
public class IOTest {

    private static String name = "";
    private static final String READ_FILE_TEST = "/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/IOLib/src/test/java/com/jimmy/development/iolib/ReadTest.txt";
    private static final int SIZE = 8 * 1024;//8k byte is the best.

    @Test
    public void testReadLine() {
        int[] lineNums = {0, 1, 10};//file line's size is 5.
        for (int i : lineNums) {

            String line = readSpecificLine(READ_FILE_TEST, i);//从0开始

            String line2 = readSpecificLine2(READ_FILE_TEST, i);

            System.out.println("line:" + line);
            System.out.println("line2:" + line2);

            if (i == 10) {
                Assert.assertNull(line);
                Assert.assertNull(line2);
            } else {
                Assert.assertEquals(line, i + "");
                Assert.assertEquals(line2, i + "");
            }


        }


    }

    @Test
    public void testReadMultiLines() {
        //读多行

    }

    @Test
    public void testFileSize() {

    }

    @Test
    public void testFileMD5() {

    }

    @Test
    public void testNio() {

    }

    @Test
    public void testScanner() {
        //Scanner scanner = new Scanner();
    }

    @Test
    public void testWriteFile() {

    }

    public void testEncode() {

    }

    public void testGzip() {
        
    }

    public void testSerialize() {

    }


    //InputStream/OutputStream 面向字节,所以对于readLine这种操作是没有的,read和skip都是针对字节的.
    //InputStreamReader/OutputStreamWrite 转换InputStream-->Reader

    //Reader/Write 面向字符

    //RandomAccessFile 适用于大小已知的文件. seek
    //RandomAccessFile 1.4之后大部分功能由nio存储隐射文件取代


    //LineNumberReader 行数相关

    // Scanner 类

    //通过nio读写文件
    //通道,缓冲器 ByteBuffer
    //引入nio之后,FileInputStream/FileOutputStream/RandomAccessFile 被修改,用来产生 FileChannel
    //FileChannel

    //编解码
    //获取基本类型
    //视图缓冲器
    //...
    //内存映射文件 -- 解决超大文件
    //文件加锁
    //部分加锁
    //压缩//Gzip
    //序列化 transient


    //读文件
    //按行读取
    //读取特定行
    //跳过
    //读文件大小
    //读文件MD5

    //写文件
    //写最后一行
    //写第一行
    //写特定行


    /**
     * FileInputStream with byte reads
     * FileInputStream opens a file by name or File object. It's read() method reads byte after byte from the file.
     * FileInputStream uses synchronization to make it thread-safe.
     *
     * @return file size
     */
    private static long read() {
        long checkSum = 0L;
        try {
            FileInputStream f = new FileInputStream(name);
            int b;
            while ((b = f.read()) != -1)
                checkSum += b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return checkSum;
    }

    /**
     * FileInputStream with byte array reads
     * FileInputStream does an I/O operation on every read and it synchronizes on all method calls to make it thread-safe.
     * To reduce this overhead, read multiple bytes at once into a buffer array of bytes
     *
     * @return
     */
    private static long read2() {
        long checkSum = 0L;
        try {
            FileInputStream f = new FileInputStream(name);
            byte[] barray = new byte[SIZE];

            int nRead;
            while ((nRead = f.read(barray, 0, SIZE)) != -1)
                for (int i = 0; i < nRead; i++)
                    checkSum += barray[i];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkSum;
    }


    //---- readLine需要针对字符的 Reader Scanner 或者 RandomAccessFile
    //BufferedReader LineNumberReader
    private static String readSpecificLine(String fileName, int line) {
        String data = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            int i = 0;
            while ((data = reader.readLine()) != null && i < line) {
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    private static String readSpecificLine2(String fileName, int line) {
        String data = null;
        LineNumberReader reader = null;

        try {
            reader = new LineNumberReader(new FileReader(fileName));
            reader.setLineNumber(line);
            int i = 0;
            while ((data = reader.readLine()) != null && i < line) {
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }


    //FileReader f=new FileReader("test.txt");
    //LineNumberReader l=new LineNumberReader(f);

    /*Conclusions
    For the best Java read performance, there are four things to remember:

    Minimize I/O operations by reading an array at a time, not a byte at a time. An 8Kbyte array is a good size.
    Minimize method calls by getting data an array at a time, not a byte at a time. Use array indexing to get at bytes in the array.
    Minimize thread synchronization locks if you don't need thread safety. Either make fewer method calls to a thread-safe class, or use a non-thread-safe class like FileChannel and MappedByteBuffer.
    Minimize data copying between the JVM/OS, internal buffers, and application arrays. Use FileChannel with memory mapping, or a direct or wrapped array ByteBuffer.*/


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

    //InputStream 的 available 非线程安全
    //InputStream 的available: 在没有阻塞的情况下所能读取的字节数,要谨慎使用
    public static long getFileSize2(String path) {//错误示范
        int len = -1;
        try {
            FileInputStream fis = new FileInputStream(path);
            len = fis.available();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return len;
    }
}
