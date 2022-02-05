package com.javalearn;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        //Path testFilePath = (Path) Paths.get("C:\\java_projects\\tmp\\test.txt");

        Path testFilePath = Paths.get("C:\\java_projects\\tmp\\test.txt");

        Path fileName = testFilePath.getFileName();
        System.out.println(fileName);

        Path parent = testFilePath.getParent();
        System.out.println(parent);

        Path root = testFilePath.getRoot();
        System.out.println(root);

        boolean endWithTxt = testFilePath.endsWith("Desktop\\testFile.txt");
        System.out.println(endWithTxt);

        boolean startsWithLalala = testFilePath.startsWith("lalalala");
        System.out.println(startsWithLalala);
    }

}
