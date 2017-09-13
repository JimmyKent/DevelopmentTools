package com.jimmy.development.iolib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly
 */
public class IOTest {

    private static String name = "";
    private static final int SIZE = 8 * 1024;//8k byte is the best.

    public static void main(String[] args) {


    }
    //InputStream/OutputStream 面向字节
    //InputStreamReader/OutputStreamWrite 转换InputStream-->Reader
    //Reader/Write 面向字符

    //RandomAccessFile 适用于大小已知的文件. seek
    //RandomAccessFile 1.4之后大部分功能由nio存储隐射文件取代

    //InputStream 的available: 在没有阻塞的情况下所能读取的字节数,要谨慎使用

    //LineNumberReader 行数相关

    


    //读文件 thinking in java 有几种Reader
    //按行读取
    //读取特定行
    //跳过


    //写文件
    //写最后一行
    //写第一行
    //写特定行


    //读文件大小
    //读文件MD5


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
}
