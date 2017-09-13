package com.jimmy.development.iolib.file;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinguochong on 2017/9/13.
 */

public class ListAllFilesTest {
    String PATH = "/Users/jinguochong/AndroidStudioProjects/DevelopmentTools/IOLib/src";

    private List<FileInfo> fileTree = new ArrayList<>();

    @Test
    public void testFileApi() {
        listFiles(PATH);
        for (FileInfo fileInfo : fileTree) {
            System.out.println(fileInfo.getLineIndent() + fileInfo.fileName);
        }
    }


    private List<File> listFiles(String root) {
        List<File> files = new ArrayList<>();
        listFiles(files, new File(root), 0);
        return files;
    }


    private void listFiles(List<File> files, File dir, int indent) {
        File[] listFiles = dir.listFiles();
        for (File f : listFiles) {
            if (f.isFile()) {
                files.add(f);
                fileTree.add(new FileInfo(f.getName(), indent));
            } else if (f.isDirectory()) {
                fileTree.add(new FileInfo(f.getName(), indent));
                listFiles(files, f, ++indent);
            }
        }
    }

    private class FileInfo {
        static final String INDENT = "  ";
        String fileName;
        int indent;

        FileInfo(String fileName, int indent) {
            this.fileName = fileName;
            this.indent = indent;
        }

        String getLineIndent() {
            StringBuilder lineIndent = new StringBuilder();
            for (int i = 0; i < indent; i++) {
                lineIndent.append(INDENT);
            }
            return lineIndent.toString();
        }

    }
}
