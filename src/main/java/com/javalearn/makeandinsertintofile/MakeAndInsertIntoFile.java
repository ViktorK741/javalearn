package com.javalearn.makeandinsertintofile;
        /*
         *  File file = new File( "C:/java_projects/tmp/io.txt"); -- создаем обьект класса file и присаеваем путь к файлу
         *  file.createNewFile(); -- создаем сам файл
         *  PrintWriter pw = new PrintWriter(file); -- созадем объект класса PrintWriter с параметром File.
         *  pw.println --пишем в файл
         *  pw.close(); закрываем pwpw
         *
         *
         * util.Scanner, util.ArrayList
         * java.nio.channels.FileChannel, java.util.Scanner, java.io.File, java.util.Collections, java.util.HashMap
         * */
        import javax.management.ObjectName;
        import java.io.*;


public class MakeAndInsertIntoFile {
    public static void main(String[] args) {
        FileInputStream fis = null;
        int num = 0;
        BufferedReader br = null;
        FileReader fr = null;

        try {
            File file = new File( "C:/java_projects/tmp/io.txt");
            if(!file.exists())
                file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            for(int i=0; i<10; i++){
                pw.println( i );
            }
            pw.close();

            fis = new FileInputStream("C:/java_projects/tmp/io.txt");
            fr = new FileReader("C:/java_projects/tmp/io.txt");

            while ((num = fr.read()) != -1){
                System.out.print((char)num);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


   }
}