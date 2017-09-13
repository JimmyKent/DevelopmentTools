package com.jimmy.development.iolib.file;


import android.annotation.TargetApi;
import android.os.Build;

import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by jinguochong on 2017/9/13.
 */

public class FileTest {

    String PATH  ="/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/IOLib/src/test/java/com/jimmy/development/iolib";

    public static void main(String[] args) {
        String[] list = getFileList("/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/IOLib/src/test/java/com/jimmy/development/iolib");
        String[] list2 = getFileList("/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/IOLib/src/test/java/com/jimmy/development/iolib", ".*\\.java");

        System.out.println(Arrays.toString(list));
        System.out.println(Arrays.toString(list2));
    }

    //获取当前目录所有文件
    public static String[] getFileList(String path) {
        File dir = new File(path);
        String[] list = dir.list();
        return list;
    }

    public static String[] getFileList(String path, final String regex) {
        File dir = new File(path);
        String[] list = dir.list(new FilenameFilter() {
            private Pattern pattern;

            @Override
            public boolean accept(File file, String name) {
                pattern = Pattern.compile(regex);
                return pattern.matcher(name).matches();
            }
        });
        return list;
    }

    //----遍历----

    @Test
    public void testErgodic(){
        listFiles(new File(PATH));
    }

    //1 用file api
    static Collection<File> listFiles(File root) {
        List<File> files = new ArrayList<>();
        listFiles(files, root);
        return files;
    }

    static void listFiles(List<File> files, File dir) {
        File[] listFiles = dir.listFiles();
        for (File f : listFiles) {
            if (f.isFile()) {
                files.add(f);
            } else if (f.isDirectory()) {
                listFiles(files, f);
            }
        }
    }

    //2 nio最快
    @TargetApi(Build.VERSION_CODES.O)
    public static void ergodic(Path path) throws IOException {
        final List<File> files = new ArrayList<>();
        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                files.add(file.toFile());
                return super.visitFile(file, attrs);
            }
        };

        java.nio.file.Files.walkFileTree(path, finder);
    }
}
