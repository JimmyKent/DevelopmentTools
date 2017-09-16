package com.jimmy.development.iolib;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly
 * 看api
 * XXX 考虑线程安全
 * 涉及到行,字符的,用数据库和json,xml(SharedPreference)会好些
 */
public class IOTest {

    private static String name = "";
    private static final String READ_FILE_TEST = "/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/IOLib/src/test/java/com/jimmy/development/iolib/ReadTest.txt";
    private static final String WRITE_FILE_TEST = "/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/IOLib/src/test/java/com/jimmy/development/iolib/WriteTest.txt";
    private static final String FILE_SIZE_TEST = READ_FILE_TEST;

    private static final int SIZE = 8 * 1024;//8k byte is the best.

    @Test
    public void testReadLine() {
        int[] lineNums = {0, 1, 10};//file line's size is 5.
        for (int i : lineNums) {

            String line = readSpecificLine(READ_FILE_TEST, i);//从0开始

            String line2 = readSpecificLine2(READ_FILE_TEST, i);

            String line3 = readSpecificLine3(READ_FILE_TEST, i);

            System.out.println("line:" + line);
            System.out.println("line2:" + line2);
            System.out.println("line3:" + line3);

            if (i == 10) {
                Assert.assertNull(line);
                Assert.assertNull(line2);
                Assert.assertNull(line3);
            } else {
                Assert.assertEquals(line, i + "");
                Assert.assertEquals(line2, i + "");
                Assert.assertEquals(line3, i + "");
            }


        }


    }

    @Test
    public void testReadMultiLines() {
        //读多行

    }

    @Test
    public void testFileSize() {
        long size = getFileSize(FILE_SIZE_TEST);
    }

    @Test
    public void testFileMD5() {
        // Tools/test/MD5Learning.java
    }

    @Test
    public void testNio() {

        //read
        //write


    }

    @Test
    public void testScanner() {
        String line3 = readSpecificLine3(READ_FILE_TEST, 0);
        Assert.assertEquals(line3, 0 + "");
    }

    @Test
    public void testWriteFile() {

        //OutputStream
        //Writer
        //RandomAccessFile
        //nio

        write(WRITE_FILE_TEST, "a");
        writeInEnd(WRITE_FILE_TEST, "a");
        rewriteSpecificLine(WRITE_FILE_TEST, 5, "a");
    }


    @Test
    public void testGzip() {

    }

    @Test
    public void testSerialize() {
        //见序列化
    }


    //InputStream/OutputStream 面向字节,所以对于readLine这种操作是没有的,read和skip都是针对字节的.
    //InputStreamReader/OutputStreamWrite 转换InputStream-->Reader

    //Reader/Write 面向字符,按行读取有优势
    //LineNumberReader 行数相关

    //RandomAccessFile 面向字节,但是可以读取基本类型,适用于大小已知的文件. seek
    //RandomAccessFile 1.4之后大部分功能由nio存储隐射文件取代

    // Scanner 类

    //转换
    //InputStream --> InputStreamReader --> BufferedReader
    //FileOutputStream --> OutputStreamWriter --> BufferedWriter

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
     * @return size
     */
    private static long read2() {
        long checkSum = 0L;
        try {
            FileInputStream f = new FileInputStream(name);
            byte[] bArray = new byte[SIZE];

            int nRead;
            while ((nRead = f.read(bArray, 0, SIZE)) != -1)
                for (int i = 0; i < nRead; i++)
                    checkSum += bArray[i];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkSum;
    }


    private static List<String> readByLine(String fileName) {
        List<String> list = new ArrayList<>();
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(fileName));
            String data;
            while ((data = reader.readLine()) != null) {
                list.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //---- readLine需要针对字符的 Reader Scanner 或者 RandomAccessFile XXX 都有个问题:要把文件全部读取到内存,用nio就不需要读取整个文件
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    private static String readSpecificLine3(String fileName, int line) {
        String data = null;
        try {
            Scanner scanner = new Scanner(new File(fileName));
            int i = -1;

            while (i < line) {
                data = scanner.nextLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e1) {
            e1.printStackTrace();
            data = null;
        }
        return data;
    }

    //nio读取行,只有在大文件才用 http://hr10108.iteye.com/blog/1788781
    private static String readLines4(String fileName, int line) {
        String data = null;

        return data;
    }

    private static String readSpecificLines5(String fileName, int line) {
        String data = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            //raf.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    //Java 8
    private static String readSpecificLines6(String fileName, int line) {
        /*Java 8解决方案：
        对于小文件：
        String line32 = Files.readAllLines(Paths.get("file.txt")).get(32)
        对于大文件：
        try (Stream<String> lines = Files.lines(Paths.get("file.txt"))) {
            line32 = lines.skip(31).findFirst().get();
        }*/
        return null;
    }

    //替换,覆盖所有内容
    private static void write(String fileName, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName, false);
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改某行
    private static void rewriteSpecificLine(String fileName, int line, String content) {
        //1·将TXT逐行读取出来，存入一个容器，比如ArrayList （同时替换掉你需要改的那一行的内容）
        //2·之后获取TXT的PrintWriter，遍历ArrayList，将内容写入其中，它会自动覆盖掉之前的内容

        List<String> list = readByLine(fileName);
        if (line > list.size() - 1 || line < 0) {
            return;
        }
        list.remove(line);
        list.add(line, content);

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < list.size(); i++) {
                //System.out.println("list[" + i + "]" + list.get(i));
                bw.write(list.get(i));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //插入某行
    private static void addSpecificLine(String fileName, int line, String content) {
        List<String> list = readByLine(fileName);
        if (line < 0) {
            line = 0;
        }
        if (line > list.size()) {
            line = list.size();
        }

        list.add(line, content);

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < list.size(); i++) {
                //System.out.println("list[" + i + "]" + list.get(i));
                bw.write(list.get(i));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //调用 插入某行 line == 0
    private static void writeInHeader(String fileName, String content) {
        addSpecificLine(fileName, 0, content);
    }

    //写文件结尾
    private static void writeInEnd(String fileName, String content) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName, true);
            out.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeInEnd1(String fileName, String content) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeInEnd2(String fileName, String content) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //FileWriter
    public static void writeInEnd3(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //RandomAccessFile
    public static void writeInEnd4(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void gzipFile(String fileName, String zipName) {//"test.gz"
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(zipName)));
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void gzipFiles(String dirName) {

    }

    private static void gzipFiles(String fileName, String[] fileNames) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            CheckedOutputStream csum = new CheckedOutputStream(fos, new Adler32());//crc32慢些,但是准确些
            ZipOutputStream zos = new ZipOutputStream(csum);
            BufferedOutputStream out = new BufferedOutputStream(zos);
            zos.setComment("a test of java zipping.");

            for (String arg : fileNames) {
                BufferedReader in = new BufferedReader(new FileReader(arg));
                zos.putNextEntry(new ZipEntry(arg));
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
                in.close();
                out.flush();
            }
            out.close();
            //Checksum valid only after the file has been closed!
            System.out.println("Checksum:" + csum.getChecksum().getValue());

            //now extract the files:
            FileInputStream fi = new FileInputStream(fileName);
            CheckedInputStream csumi = new CheckedInputStream(fi, new Adler32());
            ZipInputStream in2 = new ZipInputStream(csumi);
            BufferedInputStream bis = new BufferedInputStream(in2);
            ZipEntry ze;
            while ((ze = in2.getNextEntry()) != null) {
                System.out.println("reading file :" +ze);
                int x;
                while ((x = bis.read())!=-1){
                    System.out.write(x);
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //java本身没有删除的方法

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return len;
    }
}
